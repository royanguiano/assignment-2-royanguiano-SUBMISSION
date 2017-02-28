package lexer.setup;

import java.util.*;
import java.io.*;

/**
*  TokenSetup class is used to read the tokens from file <i>tokens</i>
*  and automatically build the 2 classes/files <i>TokenType.java</i>
*  and <i>Sym.java</i><br>
*  Therefore, if there is any change to the tokens then we only need to
*  modify the file <i>tokens</i> and run this program again before using the
*  compiler
*/
public class TokenSetup {
  // token type/value for new token
  private String type, value;
  private int tokenCount = 0;
  private BufferedReader in;
  // files used for new classes
  private PrintWriter table, symbols;

  public static void main(String args[]) {
    new TokenSetup().initTokenClasses();
  }

  TokenSetup() {
    try {
      System.out.println(
        "User's current working directory: " +
        System.getProperty( "user.dir" )
      );

      String sep = System.getProperty( "file.separator" );
      in = new BufferedReader( new FileReader( "lexer" + sep + "setup" + sep + "tokens" ));
      table = new PrintWriter( new FileOutputStream( "lexer" + sep + "TokenType.java" ));
      symbols = new PrintWriter( new FileOutputStream( "lexer" + sep + "Tokens.java" ));
    } catch( Exception e ) {
      System.out.println( e );
    }
  }

  /**
  *  read next line which contains token information;<br>
  *  each line will contain the token type used in lexical analysis and
  *  the printstring of the token: e.g.<br><ul>
  *  <li>Program program</li>
  *  <li>Int int</li>
  *  <li>BOOLean boolean</li></ul>
  */
  public void getNextToken() throws IOException {
    try {
      StringTokenizer st = new StringTokenizer(in.readLine());
      type = st.nextToken();
      value = st.nextToken();
    } catch( NoSuchElementException e ) {
      System.out.println( "***tokens file does not have 2 strings per line***" );
      System.exit( 1 );
    } catch( NullPointerException ne ) {
      // attempt to build new StringTokenizer when at end of file
      throw new IOException( "***End of File***" );
    }

    tokenCount++;
  }

  /**
  *  initTokenClasses will create the 2 files
  */
  public void initTokenClasses() {
    table.println("package lexer;");
    table.println(" ");
    table.println("/**");
    table.println(" *  This file is automatically generated<br>");
    table.println(" *  it contains the table of mappings from token");
    table.println(" *  constants to their Symbols");
    table.println("*/");
    table.println("public class TokenType {");
    table.println("   public static java.util.HashMap<Tokens,Symbol> tokens = new java.util.HashMap<Tokens,Symbol>();");
    table.println("   public TokenType() {");
    symbols.println("package lexer;");
    symbols.println(" ");
    symbols.println("/**");
    symbols.println(" *  This file is automatically generated<br>");
    symbols.println(" *  - it contains the enumberation of all of the tokens");
    symbols.println("*/");
    symbols.println("public enum Tokens {");
    symbols.print("  BogusToken");

    while (true) {
      try {
        getNextToken();
      } catch (IOException e) {break;}

      String symType = "Tokens." + type;

      table.println("     tokens.put(" + symType +
      ", Symbol.symbol(\"" + value + "\"," + symType + "));");

      if (tokenCount % 5 == 0) {
        symbols.print(",\n    "+ type);
      } else {
        symbols.print("," + type);
      }
    }

    table.println("   }");
    table.println("}");
    table.close();
    symbols.println("\n}");
    symbols.close();

    try {
      in.close();
    } catch (Exception e) {}
  }
}
