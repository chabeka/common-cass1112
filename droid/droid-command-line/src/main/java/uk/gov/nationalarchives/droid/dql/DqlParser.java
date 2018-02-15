// $ANTLR 3.2 Sep 23, 2009 14:05:07 uk\\gov\\nationalarchives\\droid\\dql\\Dql.g 2012-09-21 14:50:20

package uk.gov.nationalarchives.droid.dql;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;


import org.antlr.runtime.tree.*;

public class DqlParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "TEXT_CRIT", "INT_CRIT", "DATE_CRIT", "SET_CRIT", "SET_VALUES", "ID", "EQ", "NE", "BEGINS", "CONTAINS", "ENDS", "NOT_CONTAINS", "NOT_STARTS", "NOT_ENDS", "LTE", "LT", "GTE", "GT", "ANY_OF", "NONE_OF", "STRING", "INT", "ISO8601_DATE", "ESC_SEQ", "YEAR", "MONTH", "DAY", "HOUR", "MIN", "SEC", "WS"
    };
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
    public static final int WS=34;
    public static final int ESC_SEQ=27;
    public static final int ANY_OF=22;
    public static final int SEC=33;
    public static final int ISO8601_DATE=26;
    public static final int ENDS=14;
    public static final int NOT_STARTS=16;
    public static final int NONE_OF=23;
    public static final int GT=21;
    public static final int BEGINS=12;
    public static final int EQ=10;
    public static final int NOT_CONTAINS=15;
    public static final int TEXT_CRIT=4;
    public static final int STRING=24;
    public static final int NE=11;

    // delegates
    // delegators


        public DqlParser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public DqlParser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        
    protected TreeAdaptor adaptor = new CommonTreeAdaptor();

    public void setTreeAdaptor(TreeAdaptor adaptor) {
        this.adaptor = adaptor;
    }
    public TreeAdaptor getTreeAdaptor() {
        return adaptor;
    }

    public String[] getTokenNames() { return DqlParser.tokenNames; }
    public String getGrammarFileName() { return "uk\\gov\\nationalarchives\\droid\\dql\\Dql.g"; }


    public static class entry_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "entry"
    // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:24:1: entry : criterion -> ^( criterion ) ;
    public final DqlParser.entry_return entry() throws RecognitionException {
        DqlParser.entry_return retval = new DqlParser.entry_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        DqlParser.criterion_return criterion1 = null;


        RewriteRuleSubtreeStream stream_criterion=new RewriteRuleSubtreeStream(adaptor,"rule criterion");
        try {
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:25:5: ( criterion -> ^( criterion ) )
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:25:7: criterion
            {
            pushFollow(FOLLOW_criterion_in_entry95);
            criterion1=criterion();

            state._fsp--;

            stream_criterion.add(criterion1.getTree());


            // AST REWRITE
            // elements: criterion
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 26:5: -> ^( criterion )
            {
                // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:26:8: ^( criterion )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(stream_criterion.nextNode(), root_1);

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;
            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "entry"

    public static class criterion_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "criterion"
    // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:29:1: criterion : ( text_criterion | int_criterion | date_criterion | set_criterion ) ;
    public final DqlParser.criterion_return criterion() throws RecognitionException {
        DqlParser.criterion_return retval = new DqlParser.criterion_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        DqlParser.text_criterion_return text_criterion2 = null;

        DqlParser.int_criterion_return int_criterion3 = null;

        DqlParser.date_criterion_return date_criterion4 = null;

        DqlParser.set_criterion_return set_criterion5 = null;



        try {
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:30:5: ( ( text_criterion | int_criterion | date_criterion | set_criterion ) )
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:30:9: ( text_criterion | int_criterion | date_criterion | set_criterion )
            {
            root_0 = (Object)adaptor.nil();

            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:30:9: ( text_criterion | int_criterion | date_criterion | set_criterion )
            int alt1=4;
            switch ( input.LA(1) ) {
            case ID:
                {
                switch ( input.LA(2) ) {
                case EQ:
                case NE:
                    {
                    switch ( input.LA(3) ) {
                    case STRING:
                        {
                        alt1=1;
                        }
                        break;
                    case ISO8601_DATE:
                        {
                        alt1=3;
                        }
                        break;
                    case INT:
                        {
                        alt1=2;
                        }
                        break;
                    default:
                        NoViableAltException nvae =
                            new NoViableAltException("", 1, 2, input);

                        throw nvae;
                    }

                    }
                    break;
                case ANY_OF:
                case NONE_OF:
                    {
                    alt1=4;
                    }
                    break;
                case LTE:
                case LT:
                case GTE:
                case GT:
                    {
                    switch ( input.LA(3) ) {
                    case ISO8601_DATE:
                        {
                        alt1=3;
                        }
                        break;
                    case INT:
                        {
                        alt1=2;
                        }
                        break;
                    default:
                        NoViableAltException nvae =
                            new NoViableAltException("", 1, 4, input);

                        throw nvae;
                    }

                    }
                    break;
                case BEGINS:
                case CONTAINS:
                case ENDS:
                case NOT_CONTAINS:
                case NOT_STARTS:
                case NOT_ENDS:
                    {
                    alt1=1;
                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("", 1, 1, input);

                    throw nvae;
                }

                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 1, 0, input);

                throw nvae;
            }

            switch (alt1) {
                case 1 :
                    // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:30:10: text_criterion
                    {
                    pushFollow(FOLLOW_text_criterion_in_criterion125);
                    text_criterion2=text_criterion();

                    state._fsp--;

                    adaptor.addChild(root_0, text_criterion2.getTree());

                    }
                    break;
                case 2 :
                    // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:30:27: int_criterion
                    {
                    pushFollow(FOLLOW_int_criterion_in_criterion129);
                    int_criterion3=int_criterion();

                    state._fsp--;

                    adaptor.addChild(root_0, int_criterion3.getTree());

                    }
                    break;
                case 3 :
                    // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:30:43: date_criterion
                    {
                    pushFollow(FOLLOW_date_criterion_in_criterion133);
                    date_criterion4=date_criterion();

                    state._fsp--;

                    adaptor.addChild(root_0, date_criterion4.getTree());

                    }
                    break;
                case 4 :
                    // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:30:60: set_criterion
                    {
                    pushFollow(FOLLOW_set_criterion_in_criterion137);
                    set_criterion5=set_criterion();

                    state._fsp--;

                    adaptor.addChild(root_0, set_criterion5.getTree());

                    }
                    break;

            }


            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "criterion"

    public static class text_criterion_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "text_criterion"
    // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:33:1: text_criterion : fieldname text_operator string_value -> ^( 'text' fieldname text_operator string_value ) ;
    public final DqlParser.text_criterion_return text_criterion() throws RecognitionException {
        DqlParser.text_criterion_return retval = new DqlParser.text_criterion_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        DqlParser.fieldname_return fieldname6 = null;

        DqlParser.text_operator_return text_operator7 = null;

        DqlParser.string_value_return string_value8 = null;


        RewriteRuleSubtreeStream stream_text_operator=new RewriteRuleSubtreeStream(adaptor,"rule text_operator");
        RewriteRuleSubtreeStream stream_string_value=new RewriteRuleSubtreeStream(adaptor,"rule string_value");
        RewriteRuleSubtreeStream stream_fieldname=new RewriteRuleSubtreeStream(adaptor,"rule fieldname");
        try {
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:34:5: ( fieldname text_operator string_value -> ^( 'text' fieldname text_operator string_value ) )
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:34:9: fieldname text_operator string_value
            {
            pushFollow(FOLLOW_fieldname_in_text_criterion157);
            fieldname6=fieldname();

            state._fsp--;

            stream_fieldname.add(fieldname6.getTree());
            pushFollow(FOLLOW_text_operator_in_text_criterion159);
            text_operator7=text_operator();

            state._fsp--;

            stream_text_operator.add(text_operator7.getTree());
            pushFollow(FOLLOW_string_value_in_text_criterion161);
            string_value8=string_value();

            state._fsp--;

            stream_string_value.add(string_value8.getTree());


            // AST REWRITE
            // elements: text_operator, TEXT_CRIT, string_value, fieldname
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 35:5: -> ^( 'text' fieldname text_operator string_value )
            {
                // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:35:8: ^( 'text' fieldname text_operator string_value )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(TEXT_CRIT, "TEXT_CRIT"), root_1);

                adaptor.addChild(root_1, stream_fieldname.nextTree());
                adaptor.addChild(root_1, stream_text_operator.nextTree());
                adaptor.addChild(root_1, stream_string_value.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;
            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "text_criterion"

    public static class int_criterion_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "int_criterion"
    // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:38:1: int_criterion : fieldname numeric_operator int_value -> ^( 'int' fieldname numeric_operator int_value ) ;
    public final DqlParser.int_criterion_return int_criterion() throws RecognitionException {
        DqlParser.int_criterion_return retval = new DqlParser.int_criterion_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        DqlParser.fieldname_return fieldname9 = null;

        DqlParser.numeric_operator_return numeric_operator10 = null;

        DqlParser.int_value_return int_value11 = null;


        RewriteRuleSubtreeStream stream_numeric_operator=new RewriteRuleSubtreeStream(adaptor,"rule numeric_operator");
        RewriteRuleSubtreeStream stream_int_value=new RewriteRuleSubtreeStream(adaptor,"rule int_value");
        RewriteRuleSubtreeStream stream_fieldname=new RewriteRuleSubtreeStream(adaptor,"rule fieldname");
        try {
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:39:5: ( fieldname numeric_operator int_value -> ^( 'int' fieldname numeric_operator int_value ) )
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:39:9: fieldname numeric_operator int_value
            {
            pushFollow(FOLLOW_fieldname_in_int_criterion196);
            fieldname9=fieldname();

            state._fsp--;

            stream_fieldname.add(fieldname9.getTree());
            pushFollow(FOLLOW_numeric_operator_in_int_criterion198);
            numeric_operator10=numeric_operator();

            state._fsp--;

            stream_numeric_operator.add(numeric_operator10.getTree());
            pushFollow(FOLLOW_int_value_in_int_criterion200);
            int_value11=int_value();

            state._fsp--;

            stream_int_value.add(int_value11.getTree());


            // AST REWRITE
            // elements: fieldname, int_value, numeric_operator, INT_CRIT
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 40:5: -> ^( 'int' fieldname numeric_operator int_value )
            {
                // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:40:8: ^( 'int' fieldname numeric_operator int_value )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(INT_CRIT, "INT_CRIT"), root_1);

                adaptor.addChild(root_1, stream_fieldname.nextTree());
                adaptor.addChild(root_1, stream_numeric_operator.nextTree());
                adaptor.addChild(root_1, stream_int_value.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;
            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "int_criterion"

    public static class date_criterion_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "date_criterion"
    // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:43:1: date_criterion : fieldname date_operator date_value -> ^( 'date' fieldname date_operator date_value ) ;
    public final DqlParser.date_criterion_return date_criterion() throws RecognitionException {
        DqlParser.date_criterion_return retval = new DqlParser.date_criterion_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        DqlParser.fieldname_return fieldname12 = null;

        DqlParser.date_operator_return date_operator13 = null;

        DqlParser.date_value_return date_value14 = null;


        RewriteRuleSubtreeStream stream_date_operator=new RewriteRuleSubtreeStream(adaptor,"rule date_operator");
        RewriteRuleSubtreeStream stream_date_value=new RewriteRuleSubtreeStream(adaptor,"rule date_value");
        RewriteRuleSubtreeStream stream_fieldname=new RewriteRuleSubtreeStream(adaptor,"rule fieldname");
        try {
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:44:5: ( fieldname date_operator date_value -> ^( 'date' fieldname date_operator date_value ) )
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:44:9: fieldname date_operator date_value
            {
            pushFollow(FOLLOW_fieldname_in_date_criterion235);
            fieldname12=fieldname();

            state._fsp--;

            stream_fieldname.add(fieldname12.getTree());
            pushFollow(FOLLOW_date_operator_in_date_criterion237);
            date_operator13=date_operator();

            state._fsp--;

            stream_date_operator.add(date_operator13.getTree());
            pushFollow(FOLLOW_date_value_in_date_criterion239);
            date_value14=date_value();

            state._fsp--;

            stream_date_value.add(date_value14.getTree());


            // AST REWRITE
            // elements: DATE_CRIT, fieldname, date_value, date_operator
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 45:5: -> ^( 'date' fieldname date_operator date_value )
            {
                // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:45:8: ^( 'date' fieldname date_operator date_value )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(DATE_CRIT, "DATE_CRIT"), root_1);

                adaptor.addChild(root_1, stream_fieldname.nextTree());
                adaptor.addChild(root_1, stream_date_operator.nextTree());
                adaptor.addChild(root_1, stream_date_value.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;
            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "date_criterion"

    public static class set_criterion_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "set_criterion"
    // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:48:1: set_criterion : fieldname set_operator set_values -> ^( 'set' fieldname set_operator set_values ) ;
    public final DqlParser.set_criterion_return set_criterion() throws RecognitionException {
        DqlParser.set_criterion_return retval = new DqlParser.set_criterion_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        DqlParser.fieldname_return fieldname15 = null;

        DqlParser.set_operator_return set_operator16 = null;

        DqlParser.set_values_return set_values17 = null;


        RewriteRuleSubtreeStream stream_set_operator=new RewriteRuleSubtreeStream(adaptor,"rule set_operator");
        RewriteRuleSubtreeStream stream_set_values=new RewriteRuleSubtreeStream(adaptor,"rule set_values");
        RewriteRuleSubtreeStream stream_fieldname=new RewriteRuleSubtreeStream(adaptor,"rule fieldname");
        try {
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:49:5: ( fieldname set_operator set_values -> ^( 'set' fieldname set_operator set_values ) )
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:49:9: fieldname set_operator set_values
            {
            pushFollow(FOLLOW_fieldname_in_set_criterion278);
            fieldname15=fieldname();

            state._fsp--;

            stream_fieldname.add(fieldname15.getTree());
            pushFollow(FOLLOW_set_operator_in_set_criterion280);
            set_operator16=set_operator();

            state._fsp--;

            stream_set_operator.add(set_operator16.getTree());
            pushFollow(FOLLOW_set_values_in_set_criterion282);
            set_values17=set_values();

            state._fsp--;

            stream_set_values.add(set_values17.getTree());


            // AST REWRITE
            // elements: set_operator, SET_CRIT, set_values, fieldname
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 50:5: -> ^( 'set' fieldname set_operator set_values )
            {
                // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:50:8: ^( 'set' fieldname set_operator set_values )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(SET_CRIT, "SET_CRIT"), root_1);

                adaptor.addChild(root_1, stream_fieldname.nextTree());
                adaptor.addChild(root_1, stream_set_operator.nextTree());
                adaptor.addChild(root_1, stream_set_values.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;
            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "set_criterion"

    public static class fieldname_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "fieldname"
    // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:53:1: fieldname : ID ;
    public final DqlParser.fieldname_return fieldname() throws RecognitionException {
        DqlParser.fieldname_return retval = new DqlParser.fieldname_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token ID18=null;

        Object ID18_tree=null;

        try {
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:54:5: ( ID )
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:54:9: ID
            {
            root_0 = (Object)adaptor.nil();

            ID18=(Token)match(input,ID,FOLLOW_ID_in_fieldname317); 
            ID18_tree = (Object)adaptor.create(ID18);
            adaptor.addChild(root_0, ID18_tree);


            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "fieldname"

    public static class text_operator_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "text_operator"
    // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:57:1: text_operator : ( EQ | NE | BEGINS | CONTAINS | ENDS | NOT_CONTAINS | NOT_STARTS | NOT_ENDS ) ;
    public final DqlParser.text_operator_return text_operator() throws RecognitionException {
        DqlParser.text_operator_return retval = new DqlParser.text_operator_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token set19=null;

        Object set19_tree=null;

        try {
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:58:5: ( ( EQ | NE | BEGINS | CONTAINS | ENDS | NOT_CONTAINS | NOT_STARTS | NOT_ENDS ) )
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:58:9: ( EQ | NE | BEGINS | CONTAINS | ENDS | NOT_CONTAINS | NOT_STARTS | NOT_ENDS )
            {
            root_0 = (Object)adaptor.nil();

            set19=(Token)input.LT(1);
            if ( (input.LA(1)>=EQ && input.LA(1)<=NOT_ENDS) ) {
                input.consume();
                adaptor.addChild(root_0, (Object)adaptor.create(set19));
                state.errorRecovery=false;
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "text_operator"

    public static class numeric_operator_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "numeric_operator"
    // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:61:1: numeric_operator : ( LTE | LT | EQ | GTE | GT | NE ) ;
    public final DqlParser.numeric_operator_return numeric_operator() throws RecognitionException {
        DqlParser.numeric_operator_return retval = new DqlParser.numeric_operator_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token set20=null;

        Object set20_tree=null;

        try {
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:62:5: ( ( LTE | LT | EQ | GTE | GT | NE ) )
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:62:9: ( LTE | LT | EQ | GTE | GT | NE )
            {
            root_0 = (Object)adaptor.nil();

            set20=(Token)input.LT(1);
            if ( (input.LA(1)>=EQ && input.LA(1)<=NE)||(input.LA(1)>=LTE && input.LA(1)<=GT) ) {
                input.consume();
                adaptor.addChild(root_0, (Object)adaptor.create(set20));
                state.errorRecovery=false;
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "numeric_operator"

    public static class date_operator_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "date_operator"
    // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:65:1: date_operator : ( LTE | LT | EQ | GTE | GT | NE ) ;
    public final DqlParser.date_operator_return date_operator() throws RecognitionException {
        DqlParser.date_operator_return retval = new DqlParser.date_operator_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token set21=null;

        Object set21_tree=null;

        try {
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:66:5: ( ( LTE | LT | EQ | GTE | GT | NE ) )
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:66:9: ( LTE | LT | EQ | GTE | GT | NE )
            {
            root_0 = (Object)adaptor.nil();

            set21=(Token)input.LT(1);
            if ( (input.LA(1)>=EQ && input.LA(1)<=NE)||(input.LA(1)>=LTE && input.LA(1)<=GT) ) {
                input.consume();
                adaptor.addChild(root_0, (Object)adaptor.create(set21));
                state.errorRecovery=false;
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "date_operator"

    public static class set_operator_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "set_operator"
    // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:69:1: set_operator : ( ANY_OF | NONE_OF ) ;
    public final DqlParser.set_operator_return set_operator() throws RecognitionException {
        DqlParser.set_operator_return retval = new DqlParser.set_operator_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token set22=null;

        Object set22_tree=null;

        try {
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:70:5: ( ( ANY_OF | NONE_OF ) )
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:70:9: ( ANY_OF | NONE_OF )
            {
            root_0 = (Object)adaptor.nil();

            set22=(Token)input.LT(1);
            if ( (input.LA(1)>=ANY_OF && input.LA(1)<=NONE_OF) ) {
                input.consume();
                adaptor.addChild(root_0, (Object)adaptor.create(set22));
                state.errorRecovery=false;
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "set_operator"

    public static class string_value_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "string_value"
    // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:73:1: string_value : STRING ;
    public final DqlParser.string_value_return string_value() throws RecognitionException {
        DqlParser.string_value_return retval = new DqlParser.string_value_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token STRING23=null;

        Object STRING23_tree=null;

        try {
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:74:5: ( STRING )
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:74:9: STRING
            {
            root_0 = (Object)adaptor.nil();

            STRING23=(Token)match(input,STRING,FOLLOW_STRING_in_string_value516); 
            STRING23_tree = (Object)adaptor.create(STRING23);
            adaptor.addChild(root_0, STRING23_tree);


            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "string_value"

    public static class int_value_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "int_value"
    // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:77:1: int_value : INT ;
    public final DqlParser.int_value_return int_value() throws RecognitionException {
        DqlParser.int_value_return retval = new DqlParser.int_value_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token INT24=null;

        Object INT24_tree=null;

        try {
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:78:5: ( INT )
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:78:9: INT
            {
            root_0 = (Object)adaptor.nil();

            INT24=(Token)match(input,INT,FOLLOW_INT_in_int_value536); 
            INT24_tree = (Object)adaptor.create(INT24);
            adaptor.addChild(root_0, INT24_tree);


            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "int_value"

    public static class set_values_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "set_values"
    // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:81:1: set_values : ( set_value )+ -> ^( 'values' ( set_value )+ ) ;
    public final DqlParser.set_values_return set_values() throws RecognitionException {
        DqlParser.set_values_return retval = new DqlParser.set_values_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        DqlParser.set_value_return set_value25 = null;


        RewriteRuleSubtreeStream stream_set_value=new RewriteRuleSubtreeStream(adaptor,"rule set_value");
        try {
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:82:5: ( ( set_value )+ -> ^( 'values' ( set_value )+ ) )
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:82:9: ( set_value )+
            {
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:82:9: ( set_value )+
            int cnt2=0;
            loop2:
            do {
                int alt2=2;
                switch ( input.LA(1) ) {
                case ID:
                    {
                    alt2=1;
                    }
                    break;

                }

                switch (alt2) {
            	case 1 :
            	    // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:82:9: set_value
            	    {
            	    pushFollow(FOLLOW_set_value_in_set_values560);
            	    set_value25=set_value();

            	    state._fsp--;

            	    stream_set_value.add(set_value25.getTree());

            	    }
            	    break;

            	default :
            	    if ( cnt2 >= 1 ) break loop2;
                        EarlyExitException eee =
                            new EarlyExitException(2, input);
                        throw eee;
                }
                cnt2++;
            } while (true);



            // AST REWRITE
            // elements: set_value, SET_VALUES
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 83:5: -> ^( 'values' ( set_value )+ )
            {
                // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:83:8: ^( 'values' ( set_value )+ )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(SET_VALUES, "SET_VALUES"), root_1);

                if ( !(stream_set_value.hasNext()) ) {
                    throw new RewriteEarlyExitException();
                }
                while ( stream_set_value.hasNext() ) {
                    adaptor.addChild(root_1, stream_set_value.nextTree());

                }
                stream_set_value.reset();

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;
            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "set_values"

    public static class set_value_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "set_value"
    // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:86:1: set_value : ID ;
    public final DqlParser.set_value_return set_value() throws RecognitionException {
        DqlParser.set_value_return retval = new DqlParser.set_value_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token ID26=null;

        Object ID26_tree=null;

        try {
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:87:5: ( ID )
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:87:9: ID
            {
            root_0 = (Object)adaptor.nil();

            ID26=(Token)match(input,ID,FOLLOW_ID_in_set_value593); 
            ID26_tree = (Object)adaptor.create(ID26);
            adaptor.addChild(root_0, ID26_tree);


            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "set_value"

    public static class date_value_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "date_value"
    // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:90:1: date_value : ISO8601_DATE ;
    public final DqlParser.date_value_return date_value() throws RecognitionException {
        DqlParser.date_value_return retval = new DqlParser.date_value_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token ISO8601_DATE27=null;

        Object ISO8601_DATE27_tree=null;

        try {
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:91:5: ( ISO8601_DATE )
            // uk\\gov\\nationalarchives\\droid\\dql\\Dql.g:91:9: ISO8601_DATE
            {
            root_0 = (Object)adaptor.nil();

            ISO8601_DATE27=(Token)match(input,ISO8601_DATE,FOLLOW_ISO8601_DATE_in_date_value617); 
            ISO8601_DATE27_tree = (Object)adaptor.create(ISO8601_DATE27);
            adaptor.addChild(root_0, ISO8601_DATE27_tree);


            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "date_value"

    // Delegated rules


 

    public static final BitSet FOLLOW_criterion_in_entry95 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_text_criterion_in_criterion125 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_int_criterion_in_criterion129 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_date_criterion_in_criterion133 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_criterion_in_criterion137 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_fieldname_in_text_criterion157 = new BitSet(new long[]{0x000000000003FC00L});
    public static final BitSet FOLLOW_text_operator_in_text_criterion159 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_string_value_in_text_criterion161 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_fieldname_in_int_criterion196 = new BitSet(new long[]{0x00000000003C0C00L});
    public static final BitSet FOLLOW_numeric_operator_in_int_criterion198 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_int_value_in_int_criterion200 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_fieldname_in_date_criterion235 = new BitSet(new long[]{0x00000000003C0C00L});
    public static final BitSet FOLLOW_date_operator_in_date_criterion237 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_date_value_in_date_criterion239 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_fieldname_in_set_criterion278 = new BitSet(new long[]{0x0000000000C00000L});
    public static final BitSet FOLLOW_set_operator_in_set_criterion280 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_set_values_in_set_criterion282 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_fieldname317 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_text_operator336 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_numeric_operator391 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_date_operator438 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_set_operator485 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STRING_in_string_value516 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INT_in_int_value536 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_value_in_set_values560 = new BitSet(new long[]{0x0000000000000202L});
    public static final BitSet FOLLOW_ID_in_set_value593 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ISO8601_DATE_in_date_value617 = new BitSet(new long[]{0x0000000000000002L});

}