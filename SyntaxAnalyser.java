import java.io.*;

public class SyntaxAnalyser extends AbstractSyntaxAnalyser
{

  public SyntaxAnalyser(String filename) throws IOException{
    lex = new LexicalAnalyser(filename);
  }

  public void _statementPart_() throws IOException, CompilationException{
    myGenerate.commenceNonterminal("<statement part>");
    acceptTerminal(Token.beginSymbol);
    statementList();
    acceptTerminal(Token.endSymbol);
    myGenerate.finishNonterminal("<statement part>");
  }

  public void acceptTerminal(int symbol) throws IOException, CompilationException{

    if(nextToken.symbol == symbol){
      myGenerate.insertTerminal(nextToken);

      nextToken = lex.getNextToken();
    }else{
      //Report Errors
    }

  }

  public void statementList() throws IOException, CompilationException{
    myGenerate.commenceNonterminal("<statement list>");
    statement();

    while(nextToken.symbol == Token.semicolonSymbol){
      acceptTerminal(Token.semicolonSymbol);
      statementList();
    }
    myGenerate.finishNonterminal("<statement list>");
  }

  //myGenerate.reportError(nextToken, "StatementList error");

  public void statement() throws IOException, CompilationException{
  myGenerate.commenceNonterminal("<statement>");
    switch(nextToken.symbol){
      case Token.identifier:
        assignmentStatement();
        break;
      case Token.ifSymbol:
        ifStatement();
        break;
      case Token.whileSymbol:
        whileStatement();
        break;
      case Token.callSymbol:
        procedureStatement();
        break;
      case Token.doSymbol:
        untilStatement();
        break;

      // default
    }
  myGenerate.finishNonterminal("<statement>");
  }

  public void assignmentStatement() throws IOException, CompilationException{
    myGenerate.commenceNonterminal("<assignment statement>");
    switch(nextToken.symbol){
      case Token.identifier:
        acceptTerminal(Token.identifier);
        acceptTerminal(Token.becomesSymbol);
        if(nextToken.symbol == Token.stringConstant){
          acceptTerminal(Token.stringConstant);
        }else{
          expression();
        }
        break;
    }
    myGenerate.finishNonterminal("<assignment statement>");
  }

  public void expression() throws IOException, CompilationException{
    myGenerate.commenceNonterminal("<expression>");
    term();
    while(nextToken.symbol == Token.plusSymbol){

      switch(nextToken.symbol){
        case Token.plusSymbol:
          acceptTerminal(Token.plusSymbol);
          expression();
          break;
        case Token.minusSymbol:
          acceptTerminal(Token.minusSymbol);
          expression();
          break;
        case Token.timesSymbol:
          acceptTerminal(Token.timesSymbol);
          term();
          break;
      }
    }
    myGenerate.finishNonterminal("<expression>");
  }

  public void term() throws IOException, CompilationException{
    myGenerate.commenceNonterminal("<term>");
    factor();
    while(nextToken.symbol == Token.timesSymbol){
        switch(nextToken.symbol){
          case Token.timesSymbol:
            acceptTerminal(Token.timesSymbol);
            term();
            break;
          case Token.divideSymbol:
            acceptTerminal(Token.divideSymbol);
            term();
            break;
        }
    }
    myGenerate.finishNonterminal("<term>");
  }

  public void factor() throws IOException, CompilationException{
    myGenerate.commenceNonterminal("<factor>");

    switch(nextToken.symbol){
      case Token.identifier:
        acceptTerminal(Token.identifier);
        break;
      case Token.numberConstant:
        acceptTerminal(Token.numberConstant);
        break;
      case Token.leftParenthesis:
        acceptTerminal(Token.leftParenthesis);
        expression();
        acceptTerminal(Token.rightParenthesis);
        break;
      // default
      //Error Report
    }
    myGenerate.finishNonterminal("<factor>");
  }



  public void ifStatement() throws IOException, CompilationException{
    myGenerate.commenceNonterminal("<if statement>");
    if(nextToken.symbol == Token.ifSymbol){
      acceptTerminal(Token.ifSymbol);
      condition();
      acceptTerminal(Token.thenSymbol);
      statementList();

      if(nextToken.symbol == Token.elseSymbol){
        acceptTerminal(Token.elseSymbol);
        statementList();
      }

      acceptTerminal(Token.endSymbol);
      acceptTerminal(Token.ifSymbol);
    }

    myGenerate.finishNonterminal("<if statement>");
  }

  public void condition() throws IOException, CompilationException{
    myGenerate.commenceNonterminal("<condition>");
    if(nextToken.symbol == Token.identifier){
      acceptTerminal(Token.identifier);
      conditionOperator();
      switch(nextToken.symbol){
        case Token.identifier:
          acceptTerminal(Token.identifier);
          break;
        case Token.numberConstant:
          acceptTerminal(Token.numberConstant);
          break;
        case Token.stringConstant:
          acceptTerminal(Token.stringConstant);
          break;
        //default
          //Report Error
      }
    }else{
      //Report Error
    }
    myGenerate.finishNonterminal("<condition>");
  }

  public void conditionOperator() throws IOException, CompilationException{
    myGenerate.commenceNonterminal("<condition operator>");
    switch(nextToken.symbol){
      case Token.greaterThanSymbol:
        acceptTerminal(Token.greaterThanSymbol);
        break;
      case Token.greaterEqualSymbol:
        acceptTerminal(Token.greaterEqualSymbol);
        break;
      case Token.equalSymbol:
        acceptTerminal(Token.equalSymbol);
        break;
      case Token.notEqualSymbol:
        acceptTerminal(Token.notEqualSymbol);
        break;
      case Token.lessThanSymbol:
        acceptTerminal(Token.lessThanSymbol);
        break;
      case Token.lessEqualSymbol:
        acceptTerminal(Token.lessEqualSymbol);
        break;
      //default
        //Report Error
    }
    myGenerate.finishNonterminal("<condition operator>");
  }

  public void whileStatement() throws IOException, CompilationException{
    myGenerate.commenceNonterminal("<while statement>");
      if(nextToken.symbol == Token.whileSymbol){
        acceptTerminal(Token.whileSymbol);
        condition();
        acceptTerminal(Token.loopSymbol);
        statementList();
        acceptTerminal(Token.endSymbol);
        acceptTerminal(Token.loopSymbol);
      }else{
        //Report Error
      }
      myGenerate.finishNonterminal("<while statement>");
  }

  public void procedureStatement() throws IOException, CompilationException{
    myGenerate.commenceNonterminal("<procedure statement>");
    // if(nextToken.symbol == Token.callSymbol){
    switch(nextToken.symbol){
      case Token.callSymbol:
        acceptTerminal(Token.callSymbol);
        acceptTerminal(Token.identifier);
        acceptTerminal(Token.leftParenthesis);
        argumentList();
        acceptTerminal(Token.rightParenthesis);
        break;
    }


    myGenerate.finishNonterminal("<procedure statement>");
  }

  public void argumentList() throws IOException, CompilationException{
    myGenerate.commenceNonterminal("<argument list>");
    acceptTerminal(Token.identifier);
    while(nextToken.symbol == Token.commaSymbol){
      acceptTerminal(Token.commaSymbol);
      argumentList();
    }
    myGenerate.finishNonterminal("<argument list>");
  }

  public void untilStatement()  throws IOException, CompilationException{
    myGenerate.commenceNonterminal("<until statement>");
    if(nextToken.symbol == Token.doSymbol){
      acceptTerminal(Token.doSymbol);
      statementList();
      acceptTerminal(Token.untilSymbol);
      condition();
    }else{
      //Report Error
    }
    myGenerate.finishNonterminal("<until statement>");
  }




}
