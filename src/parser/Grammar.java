/* Generated By:JavaCC: Do not edit this line. Grammar.java */
package parser;
        import java.util.Vector;
        import java.util.LinkedList;
        import token.*;

    public class Grammar implements GrammarConstants {

        public static Vector<Constant> constantSet;     /* Set of constant symbols */
        public static Vector<Variable> variableSet; /* Set of variable symbols */
        public static Vector<Function> functionSet;     /* Set of function symbols */

        public static Vector<Equation> to_select;
        public static Vector<Equation> selected;

        public Grammar(String input, Vector<Equation> to_select, Vector<Equation> selected) {
                this.functionSet = new Vector<Function>();
                this.constantSet = new Vector<Constant>();
                this.variableSet = new Vector<Variable>();

                this.to_select = to_select;
                this.selected = selected;

        try {
                new Grammar(new java.io.StringReader(input)).START();
        } catch(Exception e) {
          System.out.println("Oops: syntax error, check the formula!"+e.getMessage());
          System.exit(-1);
        }
    }

/*
void LITERAL():
{
  Token sign=null;
  Token p;
  LinkedList<Term> args=new LinkedList<Term >(); 
}
{
  (sign=<NOT>)? (p=<LOWER_WORD>)?
  {
    int weight=1;
     //it is a predicate with arguments, check if I've already read it with
      //a different number of arguments
      Predicate p2 = tmpPred.get(p.image);
      
      if( p2 != null && p2.getArgsCount() != args.size() )
      {
       throw new ParseException("The predicate \"" + p.image 
                        + "\" has been already read with " + p2.getArgsCount() + " argument(s)"); 
      }
      
      for(Term t:args) // retrive the weight of my arguments
      {
        weight += t.getWeight();
      }
    Predicate newPred = new Predicate(p.image,weight,args, (sign==null) ? false:true);
    tmpPred.put(p.image,newPred);
    return newPred;
  }
}
*/

/* Parser entry point */
  final public void START() throws ParseException {
    label_1:
    while (true) {
      EQUATION();
      if (jj_2_1(2)) {
        ;
      } else {
        break label_1;
      }
    }
    jj_consume_token(DOLLAR);
  }

  final public Equation EQUATION() throws ParseException {
        Term t1;
        Term t2;
        Token t = null;
        Equation e;
        boolean isNegative;
    t1 = TERM();
    if (jj_2_2(2)) {
      t = jj_consume_token(NOT);
    } else {
      ;
    }
    jj_consume_token(EQUALS);
    t2 = TERM();
    jj_consume_token(SEMICOLON);
        isNegative = t == null ? false : true;
        e = new Equation(t1, t2, isNegative);
        if (isNegative)
                selected.add(e);
        else
                to_select.add(e);
        {if (true) return e;}
    throw new Error("Missing return statement in function");
  }

  final public Term TERM() throws ParseException {
        Token token;
        Variable x;
        Constant c;
        Function f;
        LinkedList<Term> list;
    if (jj_2_3(2)) {
      token = jj_consume_token(UPPER_WORD);
                                    x = new Variable(token.toString()); variableSet.add(x); {if (true) return x;}
    } else if (jj_2_4(2)) {
      token = jj_consume_token(LOWER_ALFA);
                                     c = new Constant(token.toString()); constantSet.add(c); {if (true) return c;}
    } else if (jj_2_5(2)) {
      token = jj_consume_token(LOWER_WORD);
      jj_consume_token(18);
      list = ARGUMENTS();
      jj_consume_token(19);
          f = new Function(token.toString(), list);

          for (Function fun: functionSet) {
                if (fun.getSymbol().equals(f.getSymbol())) {

                        if (fun.getArity() != f.getArity()) {
                                System.err.println("Error: same function used with different arity");
                                System.exit(-1);
                        } else {
                                {if (true) return f;}
                        }
                }
          }

          functionSet.add(f); {if (true) return f;}
    } else {
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }

  final public LinkedList ARGUMENTS() throws ParseException {
        LinkedList<Term> list = new LinkedList<Term>();
        Term t1;
        LinkedList<Term> t2 = null;
    t1 = TERM();
    if (jj_2_6(2)) {
      jj_consume_token(COMMA);
      t2 = ARGUMENTS();
    } else {
      ;
    }
          if (t2 == null)
                list.add(t1);
          else {
                list.add(t1);
                list.addAll(t2);
          }
          {if (true) return list;}
    throw new Error("Missing return statement in function");
  }

  private boolean jj_2_1(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_1(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(0, xla); }
  }

  private boolean jj_2_2(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_2(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(1, xla); }
  }

  private boolean jj_2_3(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_3(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(2, xla); }
  }

  private boolean jj_2_4(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_4(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(3, xla); }
  }

  private boolean jj_2_5(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_5(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(4, xla); }
  }

  private boolean jj_2_6(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_6(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(5, xla); }
  }

  private boolean jj_3_2() {
    if (jj_scan_token(NOT)) return true;
    return false;
  }

  private boolean jj_3R_3() {
    if (jj_3R_4()) return true;
    return false;
  }

  private boolean jj_3_1() {
    if (jj_3R_2()) return true;
    return false;
  }

  private boolean jj_3_5() {
    if (jj_scan_token(LOWER_WORD)) return true;
    if (jj_scan_token(18)) return true;
    return false;
  }

  private boolean jj_3_4() {
    if (jj_scan_token(LOWER_ALFA)) return true;
    return false;
  }

  private boolean jj_3R_4() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3_3()) {
    jj_scanpos = xsp;
    if (jj_3_4()) {
    jj_scanpos = xsp;
    if (jj_3_5()) return true;
    }
    }
    return false;
  }

  private boolean jj_3_3() {
    if (jj_scan_token(UPPER_WORD)) return true;
    return false;
  }

  private boolean jj_3_6() {
    if (jj_scan_token(COMMA)) return true;
    if (jj_3R_3()) return true;
    return false;
  }

  private boolean jj_3R_2() {
    if (jj_3R_4()) return true;
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3_2()) jj_scanpos = xsp;
    if (jj_scan_token(EQUALS)) return true;
    return false;
  }

  /** Generated Token Manager. */
  public GrammarTokenManager token_source;
  SimpleCharStream jj_input_stream;
  /** Current token. */
  public Token token;
  /** Next token. */
  public Token jj_nt;
  private int jj_ntk;
  private Token jj_scanpos, jj_lastpos;
  private int jj_la;
  private int jj_gen;
  final private int[] jj_la1 = new int[0];
  static private int[] jj_la1_0;
  static {
      jj_la1_init_0();
   }
   private static void jj_la1_init_0() {
      jj_la1_0 = new int[] {};
   }
  final private JJCalls[] jj_2_rtns = new JJCalls[6];
  private boolean jj_rescan = false;
  private int jj_gc = 0;

  /** Constructor with InputStream. */
  public Grammar(java.io.InputStream stream) {
     this(stream, null);
  }
  /** Constructor with InputStream and supplied encoding */
  public Grammar(java.io.InputStream stream, String encoding) {
    try { jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source = new GrammarTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 0; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
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
    for (int i = 0; i < 0; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Constructor. */
  public Grammar(java.io.Reader stream) {
    jj_input_stream = new SimpleCharStream(stream, 1, 1);
    token_source = new GrammarTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 0; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Reinitialise. */
  public void ReInit(java.io.Reader stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 0; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Constructor with generated Token Manager. */
  public Grammar(GrammarTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 0; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Reinitialise. */
  public void ReInit(GrammarTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 0; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  private Token jj_consume_token(int kind) throws ParseException {
    Token oldToken;
    if ((oldToken = token).next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    if (token.kind == kind) {
      jj_gen++;
      if (++jj_gc > 100) {
        jj_gc = 0;
        for (int i = 0; i < jj_2_rtns.length; i++) {
          JJCalls c = jj_2_rtns[i];
          while (c != null) {
            if (c.gen < jj_gen) c.first = null;
            c = c.next;
          }
        }
      }
      return token;
    }
    token = oldToken;
    jj_kind = kind;
    throw generateParseException();
  }

  static private final class LookaheadSuccess extends java.lang.Error { }
  final private LookaheadSuccess jj_ls = new LookaheadSuccess();
  private boolean jj_scan_token(int kind) {
    if (jj_scanpos == jj_lastpos) {
      jj_la--;
      if (jj_scanpos.next == null) {
        jj_lastpos = jj_scanpos = jj_scanpos.next = token_source.getNextToken();
      } else {
        jj_lastpos = jj_scanpos = jj_scanpos.next;
      }
    } else {
      jj_scanpos = jj_scanpos.next;
    }
    if (jj_rescan) {
      int i = 0; Token tok = token;
      while (tok != null && tok != jj_scanpos) { i++; tok = tok.next; }
      if (tok != null) jj_add_error_token(kind, i);
    }
    if (jj_scanpos.kind != kind) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) throw jj_ls;
    return false;
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
  private int[] jj_lasttokens = new int[100];
  private int jj_endpos;

  private void jj_add_error_token(int kind, int pos) {
    if (pos >= 100) return;
    if (pos == jj_endpos + 1) {
      jj_lasttokens[jj_endpos++] = kind;
    } else if (jj_endpos != 0) {
      jj_expentry = new int[jj_endpos];
      for (int i = 0; i < jj_endpos; i++) {
        jj_expentry[i] = jj_lasttokens[i];
      }
      jj_entries_loop: for (java.util.Iterator<?> it = jj_expentries.iterator(); it.hasNext();) {
        int[] oldentry = (int[])(it.next());
        if (oldentry.length == jj_expentry.length) {
          for (int i = 0; i < jj_expentry.length; i++) {
            if (oldentry[i] != jj_expentry[i]) {
              continue jj_entries_loop;
            }
          }
          jj_expentries.add(jj_expentry);
          break jj_entries_loop;
        }
      }
      if (pos != 0) jj_lasttokens[(jj_endpos = pos) - 1] = kind;
    }
  }

  /** Generate ParseException. */
  public ParseException generateParseException() {
    jj_expentries.clear();
    boolean[] la1tokens = new boolean[20];
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 0; i++) {
      if (jj_la1[i] == jj_gen) {
        for (int j = 0; j < 32; j++) {
          if ((jj_la1_0[i] & (1<<j)) != 0) {
            la1tokens[j] = true;
          }
        }
      }
    }
    for (int i = 0; i < 20; i++) {
      if (la1tokens[i]) {
        jj_expentry = new int[1];
        jj_expentry[0] = i;
        jj_expentries.add(jj_expentry);
      }
    }
    jj_endpos = 0;
    jj_rescan_token();
    jj_add_error_token(0, 0);
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

  private void jj_rescan_token() {
    jj_rescan = true;
    for (int i = 0; i < 6; i++) {
    try {
      JJCalls p = jj_2_rtns[i];
      do {
        if (p.gen > jj_gen) {
          jj_la = p.arg; jj_lastpos = jj_scanpos = p.first;
          switch (i) {
            case 0: jj_3_1(); break;
            case 1: jj_3_2(); break;
            case 2: jj_3_3(); break;
            case 3: jj_3_4(); break;
            case 4: jj_3_5(); break;
            case 5: jj_3_6(); break;
          }
        }
        p = p.next;
      } while (p != null);
      } catch(LookaheadSuccess ls) { }
    }
    jj_rescan = false;
  }

  private void jj_save(int index, int xla) {
    JJCalls p = jj_2_rtns[index];
    while (p.gen > jj_gen) {
      if (p.next == null) { p = p.next = new JJCalls(); break; }
      p = p.next;
    }
    p.gen = jj_gen + xla - jj_la; p.first = token; p.arg = xla;
  }

  static final class JJCalls {
    int gen;
    Token first;
    int arg;
    JJCalls next;
  }

}
