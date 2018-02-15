/* Generated By:JavaCC: Do not edit this line. PDFParser.java */
package net.padaf.preflight.javacc;

import java.io.IOException;
import java.io.InputStream;

import net.padaf.preflight.BodyParseException;
import net.padaf.preflight.CrossRefParseException;
import net.padaf.preflight.HeaderParseException;
import net.padaf.preflight.PdfParseException;
import net.padaf.preflight.TrailerParseException;
import static net.padaf.preflight.ValidationConstants.*;
public class PDFParser implements PDFParserConstants {

   public String pdfHeader = "";

   public static boolean parse (InputStream is) throws IOException,ParseException {
                PDFParser parser = new PDFParser (is);
                parser.PDF();
                return true;
        }

    public static void main (String [] args) {
        PDFParser parser;
        String filename = null;
        long initTime = 0;
        long parseTime = 0;
        long startTime = 0;
        long stopTime = 0;
        if (args.length == 0)
        {
            System.out.println("PDF Parser  . . .");
            parser = new PDFParser(System.in);
        } else if (args.length == 1)
        {
            filename = args[0];
            System.out.println("PDF Parser :  Reading from file " + filename + " . . .");
            try
            {
                startTime = System.currentTimeMillis();
                parser = new PDFParser(new java.io.FileInputStream(filename));
                stopTime = System.currentTimeMillis();
                initTime = stopTime - startTime;
            } catch (java.io.FileNotFoundException e)
            {
                System.out.println("PDF Parser :  File " + filename + " not found.");
                return;
            }
        } else
        {
            System.out.println("PDF Parser :  Usage is one of:");
            System.out.println("         java PDFParser < inputfile");
            System.out.println("OR");
            System.out.println("         java PDFParser inputfile");
            return;
        }
        try
        {
            startTime = System.currentTimeMillis();

                        parser.PDF();

            stopTime = System.currentTimeMillis();
            parseTime = stopTime - startTime;
            System.out.println("PDF Parser ");
            System.out.print("   PDF Parser parsed " + filename + " successfully in " + (initTime + parseTime) + " ms.");
            System.out.println(" Init. : " + initTime + " ms / parse time : " + parseTime + " ms");
        } catch (ParseException e)
        {
            e.printStackTrace(System.out);
            System.out.println("PDF Parser :  Encountered errors during parse.");
        }
    }

  final public void indirect_object() throws ParseException {
    jj_consume_token(START_OBJECT);
    object_content();
    jj_consume_token(END_OBJECT);
  }

  final public void object_content() throws ParseException {
    label_1:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case SPACE:
      case OTHER_WHITE_SPACE:
      case EOL:
        ;
        break;
      default:
        jj_la1[0] = jj_gen;
        break label_1;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case SPACE:
        jj_consume_token(SPACE);
        break;
      case OTHER_WHITE_SPACE:
        jj_consume_token(OTHER_WHITE_SPACE);
        break;
      case EOL:
        jj_consume_token(EOL);
        break;
      default:
        jj_la1[1] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case OBJ_BOOLEAN:
    case OBJ_NUMERIC:
    case OBJ_STRING_HEX:
    case OBJ_STRING_LIT:
    case OBJ_ARRAY_START:
    case OBJ_NAME:
    case OBJ_NULL:
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case OBJ_BOOLEAN:
        jj_consume_token(OBJ_BOOLEAN);
        break;
      case OBJ_NUMERIC:
        jj_consume_token(OBJ_NUMERIC);
                                                 checkNumericLength();
        break;
      case OBJ_STRING_HEX:
        jj_consume_token(OBJ_STRING_HEX);
                                          checkStringHexLength();
        break;
      case OBJ_STRING_LIT:
        start_literal();
        break;
      case OBJ_ARRAY_START:
        array_of_object();
        break;
      case OBJ_NAME:
        jj_consume_token(OBJ_NAME);
                                    checkNameLength();
        break;
      case OBJ_NULL:
        jj_consume_token(OBJ_NULL);
        break;
      default:
        jj_la1[2] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      label_2:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case SPACE:
        case OTHER_WHITE_SPACE:
          ;
          break;
        default:
          jj_la1[3] = jj_gen;
          break label_2;
        }
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case SPACE:
          jj_consume_token(SPACE);
          break;
        case OTHER_WHITE_SPACE:
          jj_consume_token(OTHER_WHITE_SPACE);
          break;
        default:
          jj_la1[4] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
      }
      break;
    case START_DICTONNARY:
      dictionary_object();
      label_3:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case SPACE:
        case OTHER_WHITE_SPACE:
          ;
          break;
        default:
          jj_la1[5] = jj_gen;
          break label_3;
        }
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case SPACE:
          jj_consume_token(SPACE);
          break;
        case OTHER_WHITE_SPACE:
          jj_consume_token(OTHER_WHITE_SPACE);
          break;
        default:
          jj_la1[6] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case EOL:
      case STREAM:
        label_4:
        while (true) {
          switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
          case EOL:
            ;
            break;
          default:
            jj_la1[7] = jj_gen;
            break label_4;
          }
          jj_consume_token(EOL);
        }
        jj_consume_token(STREAM);
        jj_consume_token(END_STREAM);
                                        int i = token.image.indexOf(tokenImage[END_STREAM].substring(1,tokenImage[END_STREAM].length()-1));
                                        if (!(token.image.charAt(i-1) == 0x0a || token.image.charAt(i-1) == 0x0d)) {
                                                {if (true) throw new PdfParseException("Expected EOL before \u005c"endstream\u005c"", ERROR_SYNTAX_STREAM_DELIMITER);}
                                        }
        label_5:
        while (true) {
          switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
          case SPACE:
          case OTHER_WHITE_SPACE:
            ;
            break;
          default:
            jj_la1[8] = jj_gen;
            break label_5;
          }
          switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
          case SPACE:
            jj_consume_token(SPACE);
            break;
          case OTHER_WHITE_SPACE:
            jj_consume_token(OTHER_WHITE_SPACE);
            break;
          default:
            jj_la1[9] = jj_gen;
            jj_consume_token(-1);
            throw new ParseException();
          }
        }
        break;
      default:
        jj_la1[10] = jj_gen;
        ;
      }
      break;
    default:
      jj_la1[11] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  final public void array_of_object() throws ParseException {
 int counter = 0;
    jj_consume_token(OBJ_ARRAY_START);
    label_6:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case SPACE:
      case OTHER_WHITE_SPACE:
      case EOL:
      case OBJ_BOOLEAN:
      case OBJ_NUMERIC:
      case OBJ_STRING_HEX:
      case OBJ_STRING_LIT:
      case OBJ_ARRAY_START:
      case OBJ_NAME:
      case OBJ_NULL:
      case OBJ_REF:
      case START_DICTONNARY:
        ;
        break;
      default:
        jj_la1[12] = jj_gen;
        break label_6;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case OBJ_BOOLEAN:
        jj_consume_token(OBJ_BOOLEAN);
                               ++counter;
        break;
      case OBJ_NUMERIC:
        jj_consume_token(OBJ_NUMERIC);
                               ++counter; checkNumericLength();
        break;
      case OBJ_STRING_HEX:
        jj_consume_token(OBJ_STRING_HEX);
                                  ++counter;checkStringHexLength();
        break;
      case OBJ_ARRAY_START:
        array_of_object();
                                   ++counter;
        break;
      case START_DICTONNARY:
        dictionary_object();
                                      ++counter;
        break;
      case OBJ_NAME:
        jj_consume_token(OBJ_NAME);
                            ++counter; checkNameLength();
        break;
      case OBJ_NULL:
        jj_consume_token(OBJ_NULL);
                            ++counter;
        break;
      case OBJ_REF:
        jj_consume_token(OBJ_REF);
                           ++counter;
        break;
      case OBJ_STRING_LIT:
        start_literal();
                                   ++counter;
        break;
      case SPACE:
        jj_consume_token(SPACE);
        break;
      case OTHER_WHITE_SPACE:
        jj_consume_token(OTHER_WHITE_SPACE);
        break;
      case EOL:
        jj_consume_token(EOL);

        break;
      default:
        jj_la1[13] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
    jj_consume_token(OBJ_ARRAY_END);
         if(counter > MAX_ARRAY_ELEMENTS) {if (true) throw new PdfParseException("Array too long : " + counter, ERROR_SYNTAX_ARRAY_TOO_LONG);}
  }

  final public void start_literal() throws ParseException {
    jj_consume_token(OBJ_STRING_LIT);
    literal();
  }

  void literal() throws ParseException {
         Token currentToken = null;
         int nesting =  1;
         int literalLength = 0;

         while(true) {
            Token previous = getToken(0);
            currentToken = getToken(1);
            if (currentToken.kind == 0 ){
               throw new ParseException("EOF reach before the end of the literal string.");
            }
            literalLength += currentToken.image.getBytes().length;
            if ( currentToken.kind == OBJ_STRING_LIT ) {
               jj_consume_token(OBJ_STRING_LIT);
               if (previous != null && previous.image.getBytes()[previous.image.getBytes().length-1]!='\u005c\u005c') {
                  ++nesting;
               }
            } else if ( currentToken.kind == INNER_START_LIT ) {
               jj_consume_token(INNER_START_LIT);
               if (previous != null && previous.image.getBytes()[previous.image.getBytes().length-1]!='\u005c\u005c') {
                  ++nesting;
               }
            } else if ( currentToken.kind == END_LITERAL ) {
               if (previous != null && previous.image.getBytes()[previous.image.getBytes().length-1]!='\u005c\u005c') {
                  --nesting;
               }
               jj_consume_token(END_LITERAL);
               if (nesting == 0) {
                  this.token_source.curLexState = PDFParserConstants.DEFAULT;
                  break;
               }
            } else {
               currentToken = getNextToken();
            }
         }
         if (literalLength > MAX_STRING_LENGTH) {
            throw new PdfParseException("Literal String too long", ERROR_SYNTAX_LITERAL_TOO_LONG);
         }
  }

  void checkNameLength() throws ParseException, ParseException {
        if (token != null && token.image.getBytes().length > MAX_NAME_SIZE) {
                throw new PdfParseException("Object Name is too long : " + token.image.getBytes().length, ERROR_SYNTAX_NAME_TOO_LONG);
        } else {
                // Nothing to do
        }
  }

  void checkMagicNumberLength() throws ParseException, ParseException {
   if (token != null && token.image.getBytes().length < 4) {
      throw new PdfParseException("Not enough bytes after the Header (at least 4 bytes should be present with a value bigger than 127) : " + token.image, ERROR_SYNTAX_HEADER);
   } else {
      // Nothing to do
   }
  }

  void checkStringHexLength() throws ParseException, ParseException {
        if (token != null && ((token.image.length()-2)/2) > MAX_STRING_LENGTH) {
                throw new PdfParseException("Object String Hexa is toot long", ERROR_SYNTAX_HEXA_STRING_TOO_LONG);
        } else {
                // Nothing to do
        }
  }

  void checkNumericLength() throws ParseException, ParseException {
    if (token != null) {
        String num = token.image;
        try {
                long numAsLong = Long.parseLong(num);
                if (numAsLong > Integer.MAX_VALUE || numAsLong < Integer.MIN_VALUE) {
                        throw new PdfParseException("Numeric is too long or too small: " + num, ERROR_SYNTAX_NUMERIC_RANGE);
                }
        } catch (NumberFormatException e) {
                // may be a real, go to the next check
                try {
                    Double real = Double.parseDouble(num);
                if (real > MAX_POSITIVE_FLOAT || real < MAX_NEGATIVE_FLOAT) {
                   throw new PdfParseException("Float is too long or too small: " + num, ERROR_SYNTAX_NUMERIC_RANGE);
                }
                } catch (NumberFormatException e2) {
                        // should never happen 
                        throw new PdfParseException("Numeric has invalid format " + num, ERROR_SYNTAX_NUMERIC_RANGE);
                        }
                }
    } else {
        // Nothing to do
    }
  }

  final public void dictionary_object() throws ParseException {
 int tokenNumber = 0;
    jj_consume_token(START_DICTONNARY);
    label_7:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case SPACE:
      case OTHER_WHITE_SPACE:
      case EOL:
        ;
        break;
      default:
        jj_la1[14] = jj_gen;
        break label_7;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case SPACE:
        jj_consume_token(SPACE);
        break;
      case OTHER_WHITE_SPACE:
        jj_consume_token(OTHER_WHITE_SPACE);
        break;
      case EOL:
        jj_consume_token(EOL);
        break;
      default:
        jj_la1[15] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
    label_8:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case OBJ_NAME:
        ;
        break;
      default:
        jj_la1[16] = jj_gen;
        break label_8;
      }
      jj_consume_token(OBJ_NAME);
                                            ++tokenNumber; checkNameLength();
      label_9:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case SPACE:
        case OTHER_WHITE_SPACE:
        case EOL:
          ;
          break;
        default:
          jj_la1[17] = jj_gen;
          break label_9;
        }
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case SPACE:
          jj_consume_token(SPACE);
          break;
        case OTHER_WHITE_SPACE:
          jj_consume_token(OTHER_WHITE_SPACE);
          break;
        case EOL:
          jj_consume_token(EOL);
          break;
        default:
          jj_la1[18] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case OBJ_BOOLEAN:
        jj_consume_token(OBJ_BOOLEAN);
        break;
      case OBJ_NAME:
        jj_consume_token(OBJ_NAME);
                                                                    checkNameLength();
        break;
      case OBJ_NUMERIC:
        jj_consume_token(OBJ_NUMERIC);
                                                       checkNumericLength();
        break;
      case OBJ_STRING_HEX:
        jj_consume_token(OBJ_STRING_HEX);
                                                          checkStringHexLength();
        break;
      case OBJ_STRING_LIT:
        start_literal();
        break;
      case OBJ_ARRAY_START:
        array_of_object();
        break;
      case START_DICTONNARY:
        dictionary_object();
        break;
      case OBJ_NULL:
        jj_consume_token(OBJ_NULL);
        break;
      case OBJ_REF:
        jj_consume_token(OBJ_REF);
        break;
      default:
        jj_la1[19] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
                                 ++tokenNumber;
      label_10:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case SPACE:
        case OTHER_WHITE_SPACE:
        case EOL:
          ;
          break;
        default:
          jj_la1[20] = jj_gen;
          break label_10;
        }
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case SPACE:
          jj_consume_token(SPACE);
          break;
        case OTHER_WHITE_SPACE:
          jj_consume_token(OTHER_WHITE_SPACE);
          break;
        case EOL:
          jj_consume_token(EOL);
          break;
        default:
          jj_la1[21] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
      }
    }
    jj_consume_token(END_DICTONNARY);
                int entries = (int)(tokenNumber / 2);
                if (entries > MAX_DICT_ENTRIES) {
                        {if (true) throw new PdfParseException("Too Many Entries In Dictionary : " + entries, ERROR_SYNTAX_TOO_MANY_ENTRIES);}
                }
  }

  final public void PDF_header() throws ParseException, HeaderParseException {
    try {
      jj_consume_token(PERCENT);
      jj_consume_token(PDFA_HEADER);
                                          pdfHeader = token.image;
      jj_consume_token(EOL);
      jj_consume_token(PERCENT);
      jj_consume_token(BINARY_TAG);
      checkMagicNumberLength();
      jj_consume_token(EOL);
    } catch (ParseException e) {
                {if (true) throw new HeaderParseException (e);}
    } catch (TokenMgrError e) {
                {if (true) throw new HeaderParseException (e.getMessage());}
    }
  }

  final public void PDF_body() throws ParseException, BodyParseException {
    try {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case SPACE:
      case OTHER_WHITE_SPACE:
        label_11:
        while (true) {
          switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
          case SPACE:
            jj_consume_token(SPACE);
            break;
          case OTHER_WHITE_SPACE:
            jj_consume_token(OTHER_WHITE_SPACE);
            break;
          default:
            jj_la1[22] = jj_gen;
            jj_consume_token(-1);
            throw new ParseException();
          }
          switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
          case SPACE:
          case OTHER_WHITE_SPACE:
            ;
            break;
          default:
            jj_la1[23] = jj_gen;
            break label_11;
          }
        }
        jj_consume_token(EOL);
        break;
      default:
        jj_la1[24] = jj_gen;
        ;
      }
      label_12:
      while (true) {
        indirect_object();
        label_13:
        while (true) {
          switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
          case SPACE:
          case OTHER_WHITE_SPACE:
            ;
            break;
          default:
            jj_la1[25] = jj_gen;
            break label_13;
          }
          switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
          case SPACE:
            jj_consume_token(SPACE);
            break;
          case OTHER_WHITE_SPACE:
            jj_consume_token(OTHER_WHITE_SPACE);
            break;
          default:
            jj_la1[26] = jj_gen;
            jj_consume_token(-1);
            throw new ParseException();
          }
        }
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case EOL:
          jj_consume_token(EOL);
          break;
        default:
          jj_la1[27] = jj_gen;
          ;
        }
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case START_OBJECT:
          ;
          break;
        default:
          jj_la1[28] = jj_gen;
          break label_12;
        }
      }
    } catch (ParseException e) {
                {if (true) throw new BodyParseException (e);}
    } catch (TokenMgrError e) {
                {if (true) throw new BodyParseException (e.getMessage());}
    }
  }

  final public void PDF_cross_ref_table() throws ParseException, CrossRefParseException {
    try {
      jj_consume_token(XREF_TAG);
      jj_consume_token(EOL);
      label_14:
      while (true) {
        jj_consume_token(SUBSECTION_START);
        label_15:
        while (true) {
          switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
          case SPACE:
            ;
            break;
          default:
            jj_la1[29] = jj_gen;
            break label_15;
          }
          jj_consume_token(SPACE);
        }
        jj_consume_token(EOL);
        label_16:
        while (true) {
          jj_consume_token(FULL_LINE);
          label_17:
          while (true) {
            switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
            case SPACE:
              ;
              break;
            default:
              jj_la1[30] = jj_gen;
              break label_17;
            }
            jj_consume_token(SPACE);
          }
          jj_consume_token(EOL);
          switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
          case FULL_LINE:
            ;
            break;
          default:
            jj_la1[31] = jj_gen;
            break label_16;
          }
        }
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case SUBSECTION_START:
          ;
          break;
        default:
          jj_la1[32] = jj_gen;
          break label_14;
        }
      }
    } catch (ParseException e) {
                {if (true) throw new CrossRefParseException (e);}
    } catch (TokenMgrError e) {
                {if (true) throw new CrossRefParseException (e.getMessage());}
    }
  }

  final public void PDF_trailer_dictionnary() throws ParseException, TrailerParseException {
    try {
      jj_consume_token(TRAILER_TAG);
      jj_consume_token(EOL);
      dictionary_object();
      label_18:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case SPACE:
          ;
          break;
        default:
          jj_la1[33] = jj_gen;
          break label_18;
        }
        jj_consume_token(SPACE);
      }
      jj_consume_token(EOL);
    } catch (ParseException e) {
                {if (true) throw new TrailerParseException (e);}
    } catch (TokenMgrError e) {
                {if (true) throw new TrailerParseException (e.getMessage());}
    }
  }

  final public void PDF_Trailer_XRefOffset() throws ParseException, TrailerParseException {
    try {
      jj_consume_token(STARTXREF_TAG);
      jj_consume_token(EOL);
      jj_consume_token(OBJ_NUMBER);
      jj_consume_token(EOL);
      jj_consume_token(EOF_TRAILER_TAG);
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case EOL:
        jj_consume_token(EOL);
        break;
      default:
        jj_la1[34] = jj_gen;
        ;
      }
    } catch (ParseException e) {
      {if (true) throw new TrailerParseException (e);}
    } catch (TokenMgrError e) {
      {if (true) throw new TrailerParseException (e.getMessage());}
    }
  }

  final public void PDF_linearized_modified() throws ParseException, PdfParseException {
int foundXref=0;
int foundTrailer=0;
    try {
      label_19:
      while (true) {
        PDF_body();
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case XREF_TAG:
          PDF_cross_ref_table();
                                           foundXref++;
          PDF_trailer_dictionnary();
                                               foundTrailer++;
          break;
        default:
          jj_la1[35] = jj_gen;
          ;
        }
        PDF_Trailer_XRefOffset();
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case SPACE:
        case OTHER_WHITE_SPACE:
        case START_OBJECT:
          ;
          break;
        default:
          jj_la1[36] = jj_gen;
          break label_19;
        }
      }
      jj_consume_token(0);
         boolean expectedXRefAndTrailer = pdfHeader.matches("PDF-1\u005c\u005c.[1-4]");
         if (expectedXRefAndTrailer && (foundXref <= 0 || foundTrailer <= 0)) {
            {if (true) throw new TrailerParseException ("Missing Xref table or Trailer keyword in the given PDF.");}
         }
    } catch (PdfParseException e) {
                {if (true) throw e;}
    } catch (ParseException e) {
                {if (true) throw new TrailerParseException (e);}
    } catch (TokenMgrError e) {
                {if (true) throw new TrailerParseException (e.getMessage());}
    }
  }

// -------------------------------------------
// ---- The PDF grammar productions start here
// -------------------------------------------
  final public void PDF() throws ParseException, PdfParseException {
    PDF_header();
    PDF_linearized_modified();
  }

  /** Generated Token Manager. */
  public PDFParserTokenManager token_source;
  SimpleCharStream jj_input_stream;
  /** Current token. */
  public Token token;
  /** Next token. */
  public Token jj_nt;
  private int jj_ntk;
  private int jj_gen;
  final private int[] jj_la1 = new int[37];
  static private int[] jj_la1_0;
  static private int[] jj_la1_1;
  static {
      jj_la1_init_0();
      jj_la1_init_1();
   }
   private static void jj_la1_init_0() {
      jj_la1_0 = new int[] {0xe,0xe,0x6f800,0x6,0x6,0x6,0x6,0x8,0x6,0x6,0x408,0x6f800,0xef80e,0xef80e,0xe,0xe,0x20000,0xe,0xe,0xef800,0xe,0xe,0x6,0x6,0x6,0x6,0x6,0x8,0x100000,0x2,0x2,0x0,0x0,0x2,0x8,0x0,0x100006,};
   }
   private static void jj_la1_init_1() {
      jj_la1_1 = new int[] {0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x40,0x40,0x40,0x0,0x0,0x0,0x0,0x0,0x40,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x2,0x4,0x0,0x0,0x1,0x0,};
   }

  /** Constructor with InputStream. */
  public PDFParser(java.io.InputStream stream) {
     this(stream, null);
  }
  /** Constructor with InputStream and supplied encoding */
  public PDFParser(java.io.InputStream stream, String encoding) {
    try { jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source = new PDFParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 37; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  public void ReInit(java.io.InputStream stream) {
     ReInit(stream, null);
  }
  /** Reinitialise. */
  public void ReInit(java.io.InputStream stream, String encoding) {
    try { jj_input_stream.ReInit(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 37; i++) jj_la1[i] = -1;
  }

  /** Constructor. */
  public PDFParser(java.io.Reader stream) {
    jj_input_stream = new SimpleCharStream(stream, 1, 1);
    token_source = new PDFParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 37; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  public void ReInit(java.io.Reader stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 37; i++) jj_la1[i] = -1;
  }

  /** Constructor with generated Token Manager. */
  public PDFParser(PDFParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 37; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  public void ReInit(PDFParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 37; i++) jj_la1[i] = -1;
  }

  private Token jj_consume_token(int kind) throws ParseException {
    Token oldToken;
    if ((oldToken = token).next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    if (token.kind == kind) {
      jj_gen++;
      return token;
    }
    token = oldToken;
    jj_kind = kind;
    throw generateParseException();
  }


/** Get the next Token. */
  final public Token getNextToken() {
    if (token.next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    jj_gen++;
    return token;
  }

/** Get the specific Token. */
  final public Token getToken(int index) {
    Token t = token;
    for (int i = 0; i < index; i++) {
      if (t.next != null) t = t.next;
      else t = t.next = token_source.getNextToken();
    }
    return t;
  }

  private int jj_ntk() {
    if ((jj_nt=token.next) == null)
      return (jj_ntk = (token.next=token_source.getNextToken()).kind);
    else
      return (jj_ntk = jj_nt.kind);
  }

  private java.util.List<int[]> jj_expentries = new java.util.ArrayList<int[]>();
  private int[] jj_expentry;
  private int jj_kind = -1;

  /** Generate ParseException. */
  public ParseException generateParseException() {
    jj_expentries.clear();
    boolean[] la1tokens = new boolean[43];
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 37; i++) {
      if (jj_la1[i] == jj_gen) {
        for (int j = 0; j < 32; j++) {
          if ((jj_la1_0[i] & (1<<j)) != 0) {
            la1tokens[j] = true;
          }
          if ((jj_la1_1[i] & (1<<j)) != 0) {
            la1tokens[32+j] = true;
          }
        }
      }
    }
    for (int i = 0; i < 43; i++) {
      if (la1tokens[i]) {
        jj_expentry = new int[1];
        jj_expentry[0] = i;
        jj_expentries.add(jj_expentry);
      }
    }
    int[][] exptokseq = new int[jj_expentries.size()][];
    for (int i = 0; i < jj_expentries.size(); i++) {
      exptokseq[i] = jj_expentries.get(i);
    }
    return new ParseException(token, exptokseq, tokenImage);
  }

  /** Enable tracing. */
  final public void enable_tracing() {
  }

  /** Disable tracing. */
  final public void disable_tracing() {
  }

}
