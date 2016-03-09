public class Generate extends AbstractGenerate
{
  public Generate(){
    
  }
  public void reportError(Token token, String explanatoryMessage) throws CompilationException{
    throw new CompilationException(explanatoryMessage);
  }
}
