// $ANTLR 3.2 Sep 23, 2009 14:05:07 uk\\gov\\nationalarchives\\droid\\dql\\Dql.g 2012-09-21 14:50:20

package uk.gov.nationalarchives.droid.dql;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class DqlLexer extends Lexer {
    public static final int NOT_ENDS=17;
    public static final int LT=19;
    public static final int YEAR=28;
    public static final int DATE_CRIT=6;
    public static final int SET_VALUES=8;
    public static final int CONTAINS=13;
    public static final int GTE=20;
    public static final int DAY=30;
    public static final int INT=25;
    public static final int MIN=32;
    public static final int HOUR=31;
    public static final int ID=9;
    public static final int EOF=-1;
    public static final int SET_CRIT=7;
    public static final int LTE=18;
    public static final int MONTH=29;
    public static final int INT_CRIT=5;
    public static final int ESC_SEQ=27;
    public static final int WS=34;
    public static final int ANY_OF=22;
    public static final int SEC=33;
    public static final int ISO8601_DATE=26;
    public static final int ENDS=14;
    public static final int NOT_STARTS=16;
    public static final int NONE_OF=23;
    public static final int BEGINS=12;
    public static final int GT=21;
    public static final int EQ=10;
    public static final int NOT_CONTAINS=15;
    public static final int TEXT_CRIT=4;
    public static final int STRING=24;
    public static final int NE=11;

    // delegates
    // delegators

    public DqlLexer() {;} 
    public DqlLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public DqlLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);

    }
    public String getGrammarFileName() { return "uk\\gov\\nationalarchives\\droid\\dql\\Dql.g"; }

    // $ANTLR start "TEXT_CRIT"
    public final void mTEXT_CRIT() throws RecognitionException {
        try {
            int _type = TEXT_CRIT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:7:11: ( 'text' )
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:7:13: 'text'
            {
            match("text"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "TEXT_CRIT"

    // $ANTLR start "INT_CRIT"
    public final void mINT_CRIT() throws RecognitionException {
        try {
            int _type = INT_CRIT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:8:10: ( 'int' )
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:8:12: 'int'
            {
            match("int"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_CRIT"

    // $ANTLR start "DATE_CRIT"
    public final void mDATE_CRIT() throws RecognitionException {
        try {
            int _type = DATE_CRIT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:9:11: ( 'date' )
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:9:13: 'date'
            {
            match("date"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "DATE_CRIT"

    // $ANTLR start "SET_CRIT"
    public final void mSET_CRIT() throws RecognitionException {
        try {
            int _type = SET_CRIT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:10:10: ( 'set' )
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:10:12: 'set'
            {
            match("set"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "SET_CRIT"

    // $ANTLR start "SET_VALUES"
    public final void mSET_VALUES() throws RecognitionException {
        try {
            int _type = SET_VALUES;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:11:12: ( 'values' )
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:11:14: 'values'
            {
            match("values"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "SET_VALUES"

    // $ANTLR start "ANY_OF"
    public final void mANY_OF() throws RecognitionException {
        try {
            int _type = ANY_OF;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:94:14: ( 'any' | 'ANY' )
            int alt1=2;
            switch ( input.LA(1) ) {
            case 'a':
                {
                alt1=1;
                }
                break;
            case 'A':
                {
                alt1=2;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 1, 0, input);

                throw nvae;
            }

            switch (alt1) {
                case 1 :
                    // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:94:16: 'any'
                    {
                    match("any"); 


                    }
                    break;
                case 2 :
                    // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:94:29: 'ANY'
                    {
                    match("ANY"); 


                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "ANY_OF"

    // $ANTLR start "NONE_OF"
    public final void mNONE_OF() throws RecognitionException {
        try {
            int _type = NONE_OF;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:95:14: ( 'none' | 'NONE' )
            int alt2=2;
            switch ( input.LA(1) ) {
            case 'n':
                {
                alt2=1;
                }
                break;
            case 'N':
                {
                alt2=2;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 2, 0, input);

                throw nvae;
            }

            switch (alt2) {
                case 1 :
                    // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:95:16: 'none'
                    {
                    match("none"); 


                    }
                    break;
                case 2 :
                    // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:95:29: 'NONE'
                    {
                    match("NONE"); 


                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "NONE_OF"

    // $ANTLR start "CONTAINS"
    public final void mCONTAINS() throws RecognitionException {
        try {
            int _type = CONTAINS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:96:14: ( 'contains' | 'CONTAINS' )
            int alt3=2;
            switch ( input.LA(1) ) {
            case 'c':
                {
                alt3=1;
                }
                break;
            case 'C':
                {
                alt3=2;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 3, 0, input);

                throw nvae;
            }

            switch (alt3) {
                case 1 :
                    // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:96:16: 'contains'
                    {
                    match("contains"); 


                    }
                    break;
                case 2 :
                    // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:96:29: 'CONTAINS'
                    {
                    match("CONTAINS"); 


                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "CONTAINS"

    // $ANTLR start "BEGINS"
    public final void mBEGINS() throws RecognitionException {
        try {
            int _type = BEGINS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:97:14: ( 'starts ' | 'STARTS' )
            int alt4=2;
            switch ( input.LA(1) ) {
            case 's':
                {
                alt4=1;
                }
                break;
            case 'S':
                {
                alt4=2;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 4, 0, input);

                throw nvae;
            }

            switch (alt4) {
                case 1 :
                    // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:97:16: 'starts '
                    {
                    match("starts "); 


                    }
                    break;
                case 2 :
                    // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:97:29: 'STARTS'
                    {
                    match("STARTS"); 


                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "BEGINS"

    // $ANTLR start "ENDS"
    public final void mENDS() throws RecognitionException {
        try {
            int _type = ENDS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:98:14: ( 'ends' | 'ENDS' )
            int alt5=2;
            switch ( input.LA(1) ) {
            case 'e':
                {
                alt5=1;
                }
                break;
            case 'E':
                {
                alt5=2;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 5, 0, input);

                throw nvae;
            }

            switch (alt5) {
                case 1 :
                    // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:98:16: 'ends'
                    {
                    match("ends"); 


                    }
                    break;
                case 2 :
                    // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:98:29: 'ENDS'
                    {
                    match("ENDS"); 


                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "ENDS"

    // $ANTLR start "NOT_CONTAINS"
    public final void mNOT_CONTAINS() throws RecognitionException {
        try {
            int _type = NOT_CONTAINS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:99:14: ( 'not contains' | 'NOT CONTAINS' )
            int alt6=2;
            switch ( input.LA(1) ) {
            case 'n':
                {
                alt6=1;
                }
                break;
            case 'N':
                {
                alt6=2;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 6, 0, input);

                throw nvae;
            }

            switch (alt6) {
                case 1 :
                    // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:99:16: 'not contains'
                    {
                    match("not contains"); 


                    }
                    break;
                case 2 :
                    // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:99:33: 'NOT CONTAINS'
                    {
                    match("NOT CONTAINS"); 


                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "NOT_CONTAINS"

    // $ANTLR start "NOT_STARTS"
    public final void mNOT_STARTS() throws RecognitionException {
        try {
            int _type = NOT_STARTS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:100:14: ( 'not starts' | 'NOT STARTS' )
            int alt7=2;
            switch ( input.LA(1) ) {
            case 'n':
                {
                alt7=1;
                }
                break;
            case 'N':
                {
                alt7=2;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 7, 0, input);

                throw nvae;
            }

            switch (alt7) {
                case 1 :
                    // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:100:16: 'not starts'
                    {
                    match("not starts"); 


                    }
                    break;
                case 2 :
                    // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:100:33: 'NOT STARTS'
                    {
                    match("NOT STARTS"); 


                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "NOT_STARTS"

    // $ANTLR start "NOT_ENDS"
    public final void mNOT_ENDS() throws RecognitionException {
        try {
            int _type = NOT_ENDS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:101:14: ( 'not ends' | 'NOT ENDS' )
            int alt8=2;
            switch ( input.LA(1) ) {
            case 'n':
                {
                alt8=1;
                }
                break;
            case 'N':
                {
                alt8=2;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 8, 0, input);

                throw nvae;
            }

            switch (alt8) {
                case 1 :
                    // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:101:16: 'not ends'
                    {
                    match("not ends"); 


                    }
                    break;
                case 2 :
                    // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:101:33: 'NOT ENDS'
                    {
                    match("NOT ENDS"); 


                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "NOT_ENDS"

    // $ANTLR start "LTE"
    public final void mLTE() throws RecognitionException {
        try {
            int _type = LTE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:103:5: ( '<=' )
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:103:9: '<='
            {
            match("<="); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "LTE"

    // $ANTLR start "LT"
    public final void mLT() throws RecognitionException {
        try {
            int _type = LT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:104:5: ( '<' )
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:104:9: '<'
            {
            match('<'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "LT"

    // $ANTLR start "EQ"
    public final void mEQ() throws RecognitionException {
        try {
            int _type = EQ;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:105:5: ( '=' )
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:105:9: '='
            {
            match('='); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "EQ"

    // $ANTLR start "GTE"
    public final void mGTE() throws RecognitionException {
        try {
            int _type = GTE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:106:5: ( '>=' )
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:106:9: '>='
            {
            match(">="); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "GTE"

    // $ANTLR start "GT"
    public final void mGT() throws RecognitionException {
        try {
            int _type = GT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:107:5: ( '>' )
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:107:9: '>'
            {
            match('>'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "GT"

    // $ANTLR start "NE"
    public final void mNE() throws RecognitionException {
        try {
            int _type = NE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:108:5: ( '<>' )
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:108:9: '<>'
            {
            match("<>"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "NE"

    // $ANTLR start "ID"
    public final void mID() throws RecognitionException {
        try {
            int _type = ID;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:110:5: ( ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' | '/' )* )
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:110:9: ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' | '/' )*
            {
            if ( (input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:110:33: ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' | '/' )*
            loop9:
            do {
                int alt9=2;
                switch ( input.LA(1) ) {
                case '/':
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                case 'A':
                case 'B':
                case 'C':
                case 'D':
                case 'E':
                case 'F':
                case 'G':
                case 'H':
                case 'I':
                case 'J':
                case 'K':
                case 'L':
                case 'M':
                case 'N':
                case 'O':
                case 'P':
                case 'Q':
                case 'R':
                case 'S':
                case 'T':
                case 'U':
                case 'V':
                case 'W':
                case 'X':
                case 'Y':
                case 'Z':
                case '_':
                case 'a':
                case 'b':
                case 'c':
                case 'd':
                case 'e':
                case 'f':
                case 'g':
                case 'h':
                case 'i':
                case 'j':
                case 'k':
                case 'l':
                case 'm':
                case 'n':
                case 'o':
                case 'p':
                case 'q':
                case 'r':
                case 's':
                case 't':
                case 'u':
                case 'v':
                case 'w':
                case 'x':
                case 'y':
                case 'z':
                    {
                    alt9=1;
                    }
                    break;

                }

                switch (alt9) {
            	case 1 :
            	    // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:
            	    {
            	    if ( (input.LA(1)>='/' && input.LA(1)<='9')||(input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    break loop9;
                }
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "ID"

    // $ANTLR start "STRING"
    public final void mSTRING() throws RecognitionException {
        try {
            int _type = STRING;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:114:5: ( '\\'' ( ESC_SEQ | ~ ( '\\\\' | '\\'' ) )* '\\'' )
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:114:8: '\\'' ( ESC_SEQ | ~ ( '\\\\' | '\\'' ) )* '\\''
            {
            match('\''); 
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:114:13: ( ESC_SEQ | ~ ( '\\\\' | '\\'' ) )*
            loop10:
            do {
                int alt10=3;
                int LA10_0 = input.LA(1);

                if ( (LA10_0=='\\') ) {
                    alt10=1;
                }
                else if ( ((LA10_0>='\u0000' && LA10_0<='&')||(LA10_0>='(' && LA10_0<='[')||(LA10_0>=']' && LA10_0<='\uFFFF')) ) {
                    alt10=2;
                }


                switch (alt10) {
            	case 1 :
            	    // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:114:15: ESC_SEQ
            	    {
            	    mESC_SEQ(); 

            	    }
            	    break;
            	case 2 :
            	    // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:114:25: ~ ( '\\\\' | '\\'' )
            	    {
            	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='&')||(input.LA(1)>='(' && input.LA(1)<='[')||(input.LA(1)>=']' && input.LA(1)<='\uFFFF') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    break loop10;
                }
            } while (true);

            match('\''); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "STRING"

    // $ANTLR start "INT"
    public final void mINT() throws RecognitionException {
        try {
            int _type = INT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:117:5: ( ( '0' .. '9' )+ )
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:117:9: ( '0' .. '9' )+
            {
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:117:9: ( '0' .. '9' )+
            int cnt11=0;
            loop11:
            do {
                int alt11=2;
                switch ( input.LA(1) ) {
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    {
                    alt11=1;
                    }
                    break;

                }

                switch (alt11) {
            	case 1 :
            	    // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:117:10: '0' .. '9'
            	    {
            	    matchRange('0','9'); 

            	    }
            	    break;

            	default :
            	    if ( cnt11 >= 1 ) break loop11;
                        EarlyExitException eee =
                            new EarlyExitException(11, input);
                        throw eee;
                }
                cnt11++;
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT"

    // $ANTLR start "ISO8601_DATE"
    public final void mISO8601_DATE() throws RecognitionException {
        try {
            int _type = ISO8601_DATE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:119:17: ( YEAR '-' MONTH '-' DAY )
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:119:21: YEAR '-' MONTH '-' DAY
            {
            mYEAR(); 
            match('-'); 
            mMONTH(); 
            match('-'); 
            mDAY(); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "ISO8601_DATE"

    // $ANTLR start "YEAR"
    public final void mYEAR() throws RecognitionException {
        try {
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:122:9: ( ( '0' .. '9' ) ( '0' .. '9' ) ( '0' .. '9' ) ( '0' .. '9' ) )
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:122:13: ( '0' .. '9' ) ( '0' .. '9' ) ( '0' .. '9' ) ( '0' .. '9' )
            {
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:122:13: ( '0' .. '9' )
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:122:14: '0' .. '9'
            {
            matchRange('0','9'); 

            }

            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:122:23: ( '0' .. '9' )
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:122:24: '0' .. '9'
            {
            matchRange('0','9'); 

            }

            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:122:33: ( '0' .. '9' )
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:122:34: '0' .. '9'
            {
            matchRange('0','9'); 

            }

            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:122:43: ( '0' .. '9' )
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:122:44: '0' .. '9'
            {
            matchRange('0','9'); 

            }


            }

        }
        finally {
        }
    }
    // $ANTLR end "YEAR"

    // $ANTLR start "MONTH"
    public final void mMONTH() throws RecognitionException {
        try {
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:125:9: ( ( '0' .. '1' ) ( '1' .. '9' ) )
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:125:13: ( '0' .. '1' ) ( '1' .. '9' )
            {
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:125:13: ( '0' .. '1' )
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:125:14: '0' .. '1'
            {
            matchRange('0','1'); 

            }

            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:125:23: ( '1' .. '9' )
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:125:24: '1' .. '9'
            {
            matchRange('1','9'); 

            }


            }

        }
        finally {
        }
    }
    // $ANTLR end "MONTH"

    // $ANTLR start "DAY"
    public final void mDAY() throws RecognitionException {
        try {
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:128:5: ( ( '0' .. '3' ) ( '0' .. '9' ) )
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:128:9: ( '0' .. '3' ) ( '0' .. '9' )
            {
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:128:9: ( '0' .. '3' )
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:128:10: '0' .. '3'
            {
            matchRange('0','3'); 

            }

            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:128:19: ( '0' .. '9' )
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:128:20: '0' .. '9'
            {
            matchRange('0','9'); 

            }


            }

        }
        finally {
        }
    }
    // $ANTLR end "DAY"

    // $ANTLR start "HOUR"
    public final void mHOUR() throws RecognitionException {
        try {
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:131:9: ( ( '0' .. '2' ) ( '0' .. '9' ) )
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:131:13: ( '0' .. '2' ) ( '0' .. '9' )
            {
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:131:13: ( '0' .. '2' )
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:131:14: '0' .. '2'
            {
            matchRange('0','2'); 

            }

            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:131:23: ( '0' .. '9' )
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:131:24: '0' .. '9'
            {
            matchRange('0','9'); 

            }


            }

        }
        finally {
        }
    }
    // $ANTLR end "HOUR"

    // $ANTLR start "MIN"
    public final void mMIN() throws RecognitionException {
        try {
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:134:5: ( ( '0' .. '5' ) ( '0' .. '9' ) )
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:134:9: ( '0' .. '5' ) ( '0' .. '9' )
            {
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:134:9: ( '0' .. '5' )
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:134:10: '0' .. '5'
            {
            matchRange('0','5'); 

            }

            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:134:19: ( '0' .. '9' )
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:134:20: '0' .. '9'
            {
            matchRange('0','9'); 

            }


            }

        }
        finally {
        }
    }
    // $ANTLR end "MIN"

    // $ANTLR start "SEC"
    public final void mSEC() throws RecognitionException {
        try {
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:137:5: ( ( '0' .. '5' ) ( '0' .. '9' ) )
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:137:9: ( '0' .. '5' ) ( '0' .. '9' )
            {
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:137:9: ( '0' .. '5' )
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:137:10: '0' .. '5'
            {
            matchRange('0','5'); 

            }

            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:137:19: ( '0' .. '9' )
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:137:20: '0' .. '9'
            {
            matchRange('0','9'); 

            }


            }

        }
        finally {
        }
    }
    // $ANTLR end "SEC"

    // $ANTLR start "WS"
    public final void mWS() throws RecognitionException {
        try {
            int _type = WS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:139:5: ( ( ' ' | '\\t' )+ )
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:139:9: ( ' ' | '\\t' )+
            {
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:139:9: ( ' ' | '\\t' )+
            int cnt12=0;
            loop12:
            do {
                int alt12=2;
                switch ( input.LA(1) ) {
                case '\t':
                case ' ':
                    {
                    alt12=1;
                    }
                    break;

                }

                switch (alt12) {
            	case 1 :
            	    // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:
            	    {
            	    if ( input.LA(1)=='\t'||input.LA(1)==' ' ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    if ( cnt12 >= 1 ) break loop12;
                        EarlyExitException eee =
                            new EarlyExitException(12, input);
                        throw eee;
                }
                cnt12++;
            } while (true);

            skip();

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "WS"

    // $ANTLR start "ESC_SEQ"
    public final void mESC_SEQ() throws RecognitionException {
        try {
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:143:5: ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\\\"' | '\\'' | '\\\\' ) )
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:143:9: '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\\\"' | '\\'' | '\\\\' )
            {
            match('\\'); 
            if ( input.LA(1)=='\"'||input.LA(1)=='\''||input.LA(1)=='\\'||input.LA(1)=='b'||input.LA(1)=='f'||input.LA(1)=='n'||input.LA(1)=='r'||input.LA(1)=='t' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "ESC_SEQ"

    public void mTokens() throws RecognitionException {
        // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:1:8: ( TEXT_CRIT | INT_CRIT | DATE_CRIT | SET_CRIT | SET_VALUES | ANY_OF | NONE_OF | CONTAINS | BEGINS | ENDS | NOT_CONTAINS | NOT_STARTS | NOT_ENDS | LTE | LT | EQ | GTE | GT | NE | ID | STRING | INT | ISO8601_DATE | WS )
        int alt13=24;
        alt13 = dfa13.predict(input);
        switch (alt13) {
            case 1 :
                // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:1:10: TEXT_CRIT
                {
                mTEXT_CRIT(); 

                }
                break;
            case 2 :
                // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:1:20: INT_CRIT
                {
                mINT_CRIT(); 

                }
                break;
            case 3 :
                // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:1:29: DATE_CRIT
                {
                mDATE_CRIT(); 

                }
                break;
            case 4 :
                // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:1:39: SET_CRIT
                {
                mSET_CRIT(); 

                }
                break;
            case 5 :
                // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:1:48: SET_VALUES
                {
                mSET_VALUES(); 

                }
                break;
            case 6 :
                // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:1:59: ANY_OF
                {
                mANY_OF(); 

                }
                break;
            case 7 :
                // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:1:66: NONE_OF
                {
                mNONE_OF(); 

                }
                break;
            case 8 :
                // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:1:74: CONTAINS
                {
                mCONTAINS(); 

                }
                break;
            case 9 :
                // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:1:83: BEGINS
                {
                mBEGINS(); 

                }
                break;
            case 10 :
                // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:1:90: ENDS
                {
                mENDS(); 

                }
                break;
            case 11 :
                // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:1:95: NOT_CONTAINS
                {
                mNOT_CONTAINS(); 

                }
                break;
            case 12 :
                // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:1:108: NOT_STARTS
                {
                mNOT_STARTS(); 

                }
                break;
            case 13 :
                // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:1:119: NOT_ENDS
                {
                mNOT_ENDS(); 

                }
                break;
            case 14 :
                // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:1:128: LTE
                {
                mLTE(); 

                }
                break;
            case 15 :
                // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:1:132: LT
                {
                mLT(); 

                }
                break;
            case 16 :
                // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:1:135: EQ
                {
                mEQ(); 

                }
                break;
            case 17 :
                // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:1:138: GTE
                {
                mGTE(); 

                }
                break;
            case 18 :
                // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:1:142: GT
                {
                mGT(); 

                }
                break;
            case 19 :
                // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:1:145: NE
                {
                mNE(); 

                }
                break;
            case 20 :
                // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:1:148: ID
                {
                mID(); 

                }
                break;
            case 21 :
                // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:1:151: STRING
                {
                mSTRING(); 

                }
                break;
            case 22 :
                // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:1:158: INT
                {
                mINT(); 

                }
                break;
            case 23 :
                // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:1:162: ISO8601_DATE
                {
                mISO8601_DATE(); 

                }
                break;
            case 24 :
                // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:1:175: WS
                {
                mWS(); 

                }
                break;

        }

    }


    protected DFA13 dfa13 = new DFA13(this);
    static final String DFA13_eotS =
        "\1\uffff\16\22\1\47\1\uffff\1\51\2\uffff\1\53\1\uffff\17\22\5\uffff"+
        "\1\53\1\uffff\1\22\1\77\1\22\1\101\2\22\2\104\11\22\1\53\1\117\1"+
        "\uffff\1\120\1\uffff\2\22\1\uffff\1\123\1\uffff\1\123\1\uffff\3"+
        "\22\2\132\1\53\2\uffff\2\22\4\uffff\3\22\2\uffff\1\22\1\142\2\22"+
        "\1\141\2\uffff\2\22\2\147\1\uffff";
    static final String DFA13_eofS =
        "\150\uffff";
    static final String DFA13_minS =
        "\1\11\1\145\1\156\1\141\1\145\1\141\1\156\1\116\1\157\1\117\1\157"+
        "\1\117\1\124\1\156\1\116\1\75\1\uffff\1\75\2\uffff\1\60\1\uffff"+
        "\1\170\3\164\1\141\1\154\1\171\1\131\1\156\1\116\1\156\1\116\1\101"+
        "\1\144\1\104\5\uffff\1\60\1\uffff\1\164\1\57\1\145\1\57\1\162\1"+
        "\165\2\57\1\145\1\40\1\105\1\40\1\164\1\124\1\122\1\163\1\123\1"+
        "\60\1\57\1\uffff\1\57\1\uffff\1\164\1\145\1\uffff\1\57\1\143\1\57"+
        "\1\103\1\141\1\101\1\124\2\57\1\55\2\uffff\2\163\4\uffff\1\151\1"+
        "\111\1\123\2\uffff\1\40\1\57\1\156\1\116\1\57\2\uffff\1\163\1\123"+
        "\2\57\1\uffff";
    static final String DFA13_maxS =
        "\1\172\1\145\1\156\1\141\1\164\1\141\1\156\1\116\1\157\1\117\1"+
        "\157\1\117\1\124\1\156\1\116\1\76\1\uffff\1\75\2\uffff\1\71\1\uffff"+
        "\1\170\3\164\1\141\1\154\1\171\1\131\1\164\1\124\1\156\1\116\1\101"+
        "\1\144\1\104\5\uffff\1\71\1\uffff\1\164\1\172\1\145\1\172\1\162"+
        "\1\165\2\172\1\145\1\40\1\105\1\40\1\164\1\124\1\122\1\163\1\123"+
        "\1\71\1\172\1\uffff\1\172\1\uffff\1\164\1\145\1\uffff\1\172\1\163"+
        "\1\172\1\123\1\141\1\101\1\124\2\172\1\55\2\uffff\2\163\4\uffff"+
        "\1\151\1\111\1\123\2\uffff\1\40\1\172\1\156\1\116\1\172\2\uffff"+
        "\1\163\1\123\2\172\1\uffff";
    static final String DFA13_acceptS =
        "\20\uffff\1\20\1\uffff\1\24\1\25\1\uffff\1\30\17\uffff\1\16\1\23"+
        "\1\17\1\21\1\22\1\uffff\1\26\23\uffff\1\2\1\uffff\1\4\2\uffff\1"+
        "\6\12\uffff\1\1\1\3\2\uffff\1\7\1\13\1\14\1\15\3\uffff\1\12\1\27"+
        "\5\uffff\1\11\1\5\4\uffff\1\10";
    static final String DFA13_specialS =
        "\150\uffff}>";
    static final String[] DFA13_transitionS = {
            "\1\25\26\uffff\1\25\6\uffff\1\23\10\uffff\12\24\2\uffff\1\17"+
            "\1\20\1\21\2\uffff\1\7\1\22\1\13\1\22\1\16\10\22\1\11\4\22\1"+
            "\14\7\22\4\uffff\1\22\1\uffff\1\6\1\22\1\12\1\3\1\15\3\22\1"+
            "\2\4\22\1\10\4\22\1\4\1\1\1\22\1\5\4\22",
            "\1\26",
            "\1\27",
            "\1\30",
            "\1\31\16\uffff\1\32",
            "\1\33",
            "\1\34",
            "\1\35",
            "\1\36",
            "\1\37",
            "\1\40",
            "\1\41",
            "\1\42",
            "\1\43",
            "\1\44",
            "\1\45\1\46",
            "",
            "\1\50",
            "",
            "",
            "\12\52",
            "",
            "\1\54",
            "\1\55",
            "\1\56",
            "\1\57",
            "\1\60",
            "\1\61",
            "\1\62",
            "\1\63",
            "\1\64\5\uffff\1\65",
            "\1\66\5\uffff\1\67",
            "\1\70",
            "\1\71",
            "\1\72",
            "\1\73",
            "\1\74",
            "",
            "",
            "",
            "",
            "",
            "\12\75",
            "",
            "\1\76",
            "\13\22\7\uffff\32\22\4\uffff\1\22\1\uffff\32\22",
            "\1\100",
            "\13\22\7\uffff\32\22\4\uffff\1\22\1\uffff\32\22",
            "\1\102",
            "\1\103",
            "\13\22\7\uffff\32\22\4\uffff\1\22\1\uffff\32\22",
            "\13\22\7\uffff\32\22\4\uffff\1\22\1\uffff\32\22",
            "\1\105",
            "\1\106",
            "\1\107",
            "\1\110",
            "\1\111",
            "\1\112",
            "\1\113",
            "\1\114",
            "\1\115",
            "\12\116",
            "\13\22\7\uffff\32\22\4\uffff\1\22\1\uffff\32\22",
            "",
            "\13\22\7\uffff\32\22\4\uffff\1\22\1\uffff\32\22",
            "",
            "\1\121",
            "\1\122",
            "",
            "\13\22\7\uffff\32\22\4\uffff\1\22\1\uffff\32\22",
            "\1\124\1\uffff\1\126\15\uffff\1\125",
            "\13\22\7\uffff\32\22\4\uffff\1\22\1\uffff\32\22",
            "\1\124\1\uffff\1\126\15\uffff\1\125",
            "\1\127",
            "\1\130",
            "\1\131",
            "\13\22\7\uffff\32\22\4\uffff\1\22\1\uffff\32\22",
            "\13\22\7\uffff\32\22\4\uffff\1\22\1\uffff\32\22",
            "\1\133",
            "",
            "",
            "\1\134",
            "\1\135",
            "",
            "",
            "",
            "",
            "\1\136",
            "\1\137",
            "\1\140",
            "",
            "",
            "\1\141",
            "\13\22\7\uffff\32\22\4\uffff\1\22\1\uffff\32\22",
            "\1\143",
            "\1\144",
            "\13\22\7\uffff\32\22\4\uffff\1\22\1\uffff\32\22",
            "",
            "",
            "\1\145",
            "\1\146",
            "\13\22\7\uffff\32\22\4\uffff\1\22\1\uffff\32\22",
            "\13\22\7\uffff\32\22\4\uffff\1\22\1\uffff\32\22",
            ""
    };

    static final short[] DFA13_eot = DFA.unpackEncodedString(DFA13_eotS);
    static final short[] DFA13_eof = DFA.unpackEncodedString(DFA13_eofS);
    static final char[] DFA13_min = DFA.unpackEncodedStringToUnsignedChars(DFA13_minS);
    static final char[] DFA13_max = DFA.unpackEncodedStringToUnsignedChars(DFA13_maxS);
    static final short[] DFA13_accept = DFA.unpackEncodedString(DFA13_acceptS);
    static final short[] DFA13_special = DFA.unpackEncodedString(DFA13_specialS);
    static final short[][] DFA13_transition;

    static {
        int numStates = DFA13_transitionS.length;
        DFA13_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA13_transition[i] = DFA.unpackEncodedString(DFA13_transitionS[i]);
        }
    }

    class DFA13 extends DFA {

        public DFA13(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 13;
            this.eot = DFA13_eot;
            this.eof = DFA13_eof;
            this.min = DFA13_min;
            this.max = DFA13_max;
            this.accept = DFA13_accept;
            this.special = DFA13_special;
            this.transition = DFA13_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( TEXT_CRIT | INT_CRIT | DATE_CRIT | SET_CRIT | SET_VALUES | ANY_OF | NONE_OF | CONTAINS | BEGINS | ENDS | NOT_CONTAINS | NOT_STARTS | NOT_ENDS | LTE | LT | EQ | GTE | GT | NE | ID | STRING | INT | ISO8601_DATE | WS );";
        }
    }
 

}