package Parser;

import java.util.*;
import java.io.*;


interface Base 
{
	public String generateCode();
}

class Cons{
	private static int count=0;
	public static final int GLOBAL_LEVEL=0;
	public static final int FUNCTION_LEVEL=1;
	public static final int DATA_MEM_SIZE=512;
	public static final int STACK_MEM_SIZE=512;
	public static final int CODE_MEM_SIZE=1024;
	public static SymbolTable table = new SymbolTable();
	public static MemoryManager mm = new MemoryManager();
	public static final String PUSH_PC = "push pc";
	public static final String POP_PC  = "pop pc";
	public static final String PUSH_AX = "push ax";
	public static final String PUSH_BP = "push ax";
	public static final String AX	   = "ax", BP="bp", CX="cx", SI="si";
	
	public static final String ax="0", bx="1", cx="2", sp="3", bp="4", si="6", pc="6", tr="7";
	
	//
	public static ArrayList code=new ArrayList();
	
	public static boolean debugOn=false;
	//
//	public static String Move(String s, String t){
//		return "mov "+s+" ,"+t+"\n";
//		}
//	public static String Mul(String s, String t){
//		return "mul "+s+" ,"+t+"\n";
//		}
//	public static String Div(String s, String t){
//		return "div "+s+" ,"+t+"\n";
//		}
//	public static String Add(String s, String t){
//		return "add "+s+" ,"+t+"\n";
//		}
//		
//	public static String Sub(String s, String t){
//		return "sub "+s+" ,"+t+"\n";
//		}
//		
//	public static String Jmp(String instr, String r, String l){
//		return instr+" "+r+" ,"+l+"\n";
//		}
	public static void error(String msg){
		System.err.println(msg);
		}	
	public static void debug(String msg){
		if(debugOn){
			System.out.println(msg);
			}
		}
	public static String push(String reg){
		String c1 = "ST "+reg+", 0("+sp+")";
		Cons.code.add(c1);
		return c1+"\n"+decReg(sp, 1);
		}
	public static String pop(String reg){
		String code=incReg(sp, 1);
		String c1="LD "+reg+", 0("+sp+")";
		Cons.code.add(c1);
		return code+c1+"\n";
		}
	public static String move(String regt, String regs){
		String c1="ADD "+regt+", "+regs+", "+regs,
		 c2="SUB "+regt+", "+regt+", "+regs;
		String code=c1+"\n";
		code+=c2+"\n";
		Cons.code.add(c1);
		Cons.code.add(c2);
		return code;
		}
		
	public static String add(String regt, String regs1, String regs2){
		String c1 = "ADD "+regt+", "+regs1+", "+regs2;
		String code=c1+"\n";
		Cons.code.add(c1);
		return code;
		}
	public static String sub(String regt, String regs1, String regs2){
		String c1="SUB "+regt+", "+regs1+", "+regs2;
		String code=c1+"\n";
		Cons.code.add(c1);
		return code;
		}
	public static String mul(String regt, String regs1, String regs2){
		String c1 = "MUL "+regt+", "+regs1+", "+regs2;
		String code=c1+"\n";
		Cons.code.add(c1);
		return code;
		}
	public static String div(String regt, String regs1, String regs2){
		String c1 = "DIV "+regt+", "+regs1+", "+regs2;
		String code=c1+"\n";
		Cons.code.add(c1);
		return code;
		}
	public static String jmp(String cmd, String regs, String label){
		String c1, c2;
//		c1=ldc(Cons.tr, 0, Cons.ax);
		if(regs.length()==0)
			c2 = cmd+" "+label;
		else
			c2 = cmd+" "+regs+", "+label;
//		Cons.code.add(c1);
		Cons.code.add(c2);
//		return c1+"\n"+c2;
		return c2;
		}
	public static String st(String regs, int d, String s){
		String c1="ST "+regs+", "+Integer.toString(d)+"("+s+")";
		String code=c1+"\n";
		Cons.code.add(c1);
		return code;
		}
	public static String ldc(String regt, int d, String s){
		String c1="LDC "+regt+", "+Integer.toString(d)+"("+s+")";
		String code=c1+"\n";
		Cons.code.add(c1);
		return code;
		}
	public static String in(String reg){
		String c1="IN "+reg+", "+reg+" ,"+reg;
		Cons.code.add(c1);
		return c1+"\n";
		}
	public static String out(String reg){
		String c1="OUT "+reg+", "+reg+" ,"+reg;
		Cons.code.add(c1);
		return c1+"\n";
		}
	public static String decReg(String reg, int size){
		String code="";
		String c1="LDC "+Cons.tr+", "+Integer.toString(size)+"(0)", c2="SUB "+reg+","+reg+","+tr;
		code += c1+"\n";
		code += c2+"\n";
		Cons.code.add(c1);
		Cons.code.add(c2);
		return code;
		}
	public static String incReg(String reg, int size){
		String code="";
		String c1="LDC "+Cons.tr+", "+Integer.toString(size)+"(0)", c2="ADD "+reg+","+reg+","+tr;
		code += c1+"\n";
		code += c2+"\n";
		Cons.code.add(c1);
		Cons.code.add(c2);
		return code;
		}
	public static String proc(String name){
		String c1;
		c1="proc:"+name;
		Cons.code.add(c1);
		return c1+"\n";
		}
	public static String endProc(String name){
		String c1="end proc "+name;
		Cons.code.add(c1);
		return c1+"\n";
		}
	public static String call(String name){
		return jmp("jmp", "", name)+"\n";
		}	

	public static String newLabel(){
		return (new String("__label_"))+String.valueOf(count++);
		}
	}
	
class TableObject
{
	public String name;
	public int level;
	public int size;
	public int addr;
	public boolean isArray;
	public boolean isGlobal;
	public TableObject(String n, int l,int s, int a){
		name = n;
		level = l;
		size = s;
		addr=a;
		isGlobal = true;
	}
	public TableObject(String n, int l, boolean isArr, int stack){
		name=n;
		level=l;
		addr=stack;
		isArray=isArr;
		isGlobal = false;
		}
	public String toString(){
		return name+" "+new String(Integer.toString(level,10))+" "+new String(Integer.toString(addr,10));
		}
}

class Prototype{
	private int type;
	private String name;
	private Params p;
	
	public Prototype(int type, String name, Params p){
		this.type = type;
		this.name = name;
		this.p = p;
		}
	public String getName(){
		return name;
		}
	}
class SymbolTable
{
	private ArrayList gtable;
	private ArrayList ltable;
	private ArrayList functions;
	private int index;
	private int Level;
	public SymbolTable(){
		gtable = new ArrayList();
		functions = new ArrayList();
		ltable=new ArrayList();
		index=0;
		Level=Cons.GLOBAL_LEVEL;
	}

	public void addGlobalObject(String name, int level, int size){
		int addr=Cons.mm.getNextData(size);
		if(addr!=-1){
			TableObject t = new TableObject(name, level, size, addr);
			gtable.add(t);
		}
	}
	
	public void addLocalObject(String name, int level, boolean isArray, int stackAddr){
		TableObject to = new TableObject(name, level, isArray, stackAddr);
		ltable.add(to);
		}
	
	public void incLevel(){
		Level++;
		}
	
	public void decLevel(){
		Level--;
		}
	public int getLevel(){
		return Level;
		}
	public boolean addFunction(Prototype ptype){
		return functions.add(ptype);
		}
	public Prototype lookUpFunction(String name){
		Iterator e = functions.iterator();
		Prototype p;
		while(e.hasNext()){
			p = (Prototype)e.next();
			if(p.getName().equals(name))
				return p;
			}
		return null;
		}
		
	public TableObject lookUpObject(String name, int level){
		index = 0;
		Iterator e = ltable.iterator();
		TableObject to;
		while(e.hasNext()){
			to = (TableObject)e.next();
			if(to.name.equals(name) && to.level<=level){
				return to;
			}
			index++;
		}
		index = 0;
		e = gtable.iterator();
		while(e.hasNext()){
			to = (TableObject)e.next();
			if(to.name.equals(name) && to.level<=level){
				return to;
			}
			index++;
		}
		return null;
	}
	
//	public void removeObject(String name, int level){
//		if(lookUpObject(name, level)!=null)
//			table.remove(index);
//	}
//	public void Print(){
//		Iterator e=table.iterator();
//		while(e.hasNext())
//			System.out.println(e.next());
//		}
}

class MemoryManager{
	private int pc=0;
	private int data=0;
	public int getNextData(int size){
		int addr=data;
		if(data+size > Cons.DATA_MEM_SIZE)
			return -1;
		data+=size;
		return addr;
		}
	public int getNextCode(int size){
		int addr=pc;
		if(pc+size > Cons.CODE_MEM_SIZE)
			return -1;
		pc+=size;
		return addr;
		}
	}
	
	
public class Program implements Base
{
	class Function{
		public int addr;
		public String name;
		public Function(int a, String n){
			addr=a;
			name=n;
			}
		}
	private ArrayList funcs = new ArrayList();
	private Declaration_list decl_list;
	public Program(Declaration_list dl){
		decl_list = dl;
		}
	public String generateCode(){
		String code=decl_list.generateCode();
//		Cons.table.Print();
		Print(Cons.code);
		return code;
		}
//	public void Assemble(){
//		Iterator e = Cons.code.iterator();
//		String cmd;
//		int index;
//		int count=0;
//		while(e.hasNext()){
//			cmd=(String)e.next();
//			index=cmd.indexOf("proc");
//			if(index!=-1){
//				funcs.add(new Function(count, cmd.subString(5)));
//				}
//			else{
//				if(cmd.startsWith("__label_")){
//					funcs.add(new Function(count, cmd.replace(':', ' ').trim()));
//					}
//				}
//			count++;
//			}
//		}
	public void Print(ArrayList ar){
		try{
		RandomAccessFile raf = new RandomAccessFile("E:\\out.ss", "rw");
		Iterator e = ar.iterator();
			while(e.hasNext()){
					raf.writeBytes(e.next().toString());
					raf.writeBytes("\n");
			}
			raf.close();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
}

class Declaration_list implements Base
{
	private ArrayList list;
	public Declaration_list(Declaration_list dl){
		list = dl.getList();
		}
	public Declaration_list(Declaration d){
		list=new ArrayList();
		list.add(d);
		}
	public void addDeclaration(Declaration d){
		list.add(d);
		}
	public ArrayList getList(){
		return list;
		}
	public String generateCode(){
		Iterator e = list.iterator();
		String code="";
		while(e.hasNext()){
			code += ((Declaration)e.next()).generateCode();
			}
		return code;
		}
}

class Declaration
{
	private Var_declaration var_decl;
	private Fun_declaration func_decl;
	public Declaration(Var_declaration vd){
		var_decl = vd;
		}
	public Declaration(Fun_declaration fd){
		func_decl = fd;
		}
	public String generateCode(){
		if(var_decl!=null){
			var_decl.addToTable();
			return new String();
		}
		else{
//			System.out.println("go to ");
			return func_decl.generateCode();
		}
		}
}

class  Var_declaration implements Base
{
	private String name;
	private int size;
	private int level;
	public Var_declaration(String name, int size){
		this.name=name;
		this.size=size;
		this.level=0;
		}
	public void addToTable(){
		level=Cons.table.getLevel();
		Cons.table.addGlobalObject(name, level, size);
		}
	public boolean isArray(){
		return size>1;
		}
	public String generateCode(){
//		return Cons.PushVar(size);
		return "";
		}
//	public String getPopInstr(){
//		return Cons.PopVar(size);
//		}
	public String getName(){
		return name;
		}
	public int getSize(){
		return size;
		}
//	public String generateCode(){
//		return new String("int "+name+"("+String.valueOf(size)+")");
//		}
	
}

class Fun_declaration implements Base
{
	int retType;
	String name;
	Params params;
	Compound_stmt cs;
	public Fun_declaration(int type, String id, Params p, Compound_stmt c){
		retType = type;
		name = id;
		params = p;
		cs = c;
	}
	public boolean initialize(){
		params.addAllToSymbolTable(Cons.FUNCTION_LEVEL);
		Prototype p = new Prototype(retType, name, params);
		Cons.table.addFunction(p);
		return true;
		}
	public String generateCode(){
		initialize();
		String code;
		code = Cons.proc(name);
		code += Cons.push(Cons.bp);
		code += cs.generateCode();
		code += Cons.endProc(name);
		return code;		
		
	}
}

class Params
{
	private ArrayList params;
	public Params(Params ps){
		params = ps.getList();
		}
	public Params(Param p){
		this();
		addParam(p);
		}
	public Params(){
		params = new ArrayList();
		}
	public ArrayList getList(){
		return params;
		}
	public void addParam(Param p){
		params.add(p);
		}
		
	public void addAllToSymbolTable(int level){
		Param p;
		Iterator e = params.iterator();
		int index=params.size()+1;
		while(e.hasNext()){
			p = (Param)e.next();
			p.addToSymbolTable(level, index--);
		}
	}
//	public int NumOfArgs(){
//		return params.size();		
//		}
}

class Param
{
	public String name;
	public boolean isArray;
	public Param(String name, boolean isArray){
		this.name = name;
		this.isArray = isArray;
		}
	public void addToSymbolTable(int level, int stackAddr){
		Cons.table.addLocalObject(name, level, isArray, stackAddr);
		}
}

class Compound_stmt implements Base
{
//	private boolean functionBody;
	private Local_declarations local_decl;
	private Statement_list stmt_list;
	public Compound_stmt(Local_declarations l, Statement_list sl){
		local_decl = l;
		stmt_list = sl;
		}
	public String generateCode(){
		String code;
		Cons.table.incLevel();
		code = local_decl.generateCode();
		code += stmt_list.generateCode();
		Cons.table.decLevel();
		return code;
		}
	
}

class Local_declarations implements Base
{
	private ArrayList lds;
	public Local_declarations(Local_declarations l){
		lds=l.getList();
		}
	public Local_declarations(){
		lds = new ArrayList();
		}
	public ArrayList getList(){
		return lds;
		}
	
	public void addVar_declaration(Var_declaration vd){
		lds.add(vd);
		}
	public String generateCode(){
		String code="";
		Iterator e = lds.iterator();
		Var_declaration vd;
		int index=0;
		while(e.hasNext()){
			vd = (Var_declaration)e.next();
			index+=vd.getSize();
			Cons.table.addLocalObject(vd.getName(), Cons.table.getLevel(), vd.isArray(), index);
			}
		if(index>0)
			code = Cons.decReg(Cons.sp, index);
		return code;
//		String code="";
//		Iterator e;
//		e=lds.iterator();
//		while(e.hasNext()){
//			code+=((Var_declaration)e.next()).generateCode();
//			}
//		return code;
		}
//	public String getPops(){
//		String code="";
//		Iterator e;
//		e=lds.iterator();
//		while(e.hasNext()){
//			code+=((Var_declaration)e.next()).getPopInstr();
//			}
//		return code;
//		}
}

class Statement_list implements Base
{
	private ArrayList stmts;
	public ArrayList getList(){
		return stmts;
		}
	public Statement_list(){
		stmts = new ArrayList();
		}
	public Statement_list(Statement_list sl){
		stmts=sl.getList();
		}
	public void addStatement(Statement s){
		stmts.add(s);
		}
	public int getNum(){
		return stmts.size();
		}
	public String generateCode(){
		String code="", temp;
		Iterator e = stmts.iterator();
		while(e.hasNext()){
			temp = ((Statement)e.next()).generateCode();
//			System.out.println("stmt list"+temp);
			code += temp;
			}
		return code;
		}
}

class Statement implements Base
{
	private String code="";
	private Expression_stmt exp;
	private Selection_stmt sel;
	private Iteration_stmt iter;
	private Return_stmt ret;
	private Compound_stmt cs;
	
	public Statement(Expression_stmt e){
		exp=e;
		}
	public Statement(Compound_stmt c){
		cs=c;
		}
	public Statement(Selection_stmt s){
		sel=s;
		}
	public Statement(Iteration_stmt i){
		iter=i;
		}
	public Statement(Return_stmt r){
		ret=r;
		}
	public String generateCode(){
		if(exp!=null)
			code=exp.generateCode();
		else if(cs!=null)
			code=cs.generateCode();
		else if(sel!=null)
			code=sel.generateCode();
		else if(iter!=null)
			code=iter.generateCode();
		else if(ret!=null)
			code=ret.generateCode();
		return code;
		}
}

class Expression_stmt implements Base
{
	private Expression exp;
	public Expression_stmt(Expression e){
		exp = e;
		}
	public Expression_stmt(){
		}
	public String generateCode(){
		if(exp!=null){
			String code = exp.generateCode();
			return code;
		}
		return new String();
		}
}

class Expression implements Base
{
	private Simple_expression simple_exp;
	private Expression exp;
	private Var var;
	public Expression(Var v, Expression e){
		var = v;
		exp = e;
		}
	public Expression(Simple_expression se){
		simple_exp = se;
		}
	public int isVar(){	//Check to see if ultimately the exp s reduced to a variable, return the size on true, 0 on false
		return simple_exp.isVar();
		}
	public String generateCode(){
		if(simple_exp!=null){
			return simple_exp.generateCode();
			}
		String code=exp.generateCode();
		code+="\n";
		code+=Cons.move(Cons.cx, Cons.ax);
		code+=var.generateCode();
		code+=Cons.add(Cons.bx, Cons.bx, Cons.si);
		code+=Cons.st(Cons.ax, 0, Cons.bx);
		return code;
		}
}

class Selection_stmt implements Base
{
	private Expression exp;
	private Statement ifStmt, elseStmt;
	
	public Selection_stmt(Expression e, Statement s){
		exp=e;
		ifStmt=s;
		}
	public Selection_stmt(Expression e, Statement s1, Statement s2){
		exp=e;
		ifStmt=s1;
		elseStmt=s2;
		}
	public String generateCode(){
		String label1, label2, code;
		label1=Cons.newLabel();
		label2=Cons.newLabel();
		code=exp.generateCode();
		code+=Cons.jmp("JEQ", Cons.ax, label1);
		code+=ifStmt.generateCode();
		if(elseStmt!=null){
			code+=Cons.jmp("jmp", "", label2);
			Cons.code.add(new String(label1+":"));
			code+=label1+":";
			code+=elseStmt.generateCode();
			code+="\n";
			code+=label2+":";
			Cons.code.add(new String(label2+":"));
			}
		else
			code+=label1+":";
			Cons.code.add(new String(label1+":"));
//		System.out.println("sel code:"+code);
		return code;
		}
}

class Iteration_stmt implements Base
{
	private Expression exp;
	private Statement stmt;
	
	public Iteration_stmt(Expression e, Statement s){
		exp = e;
		stmt = s;
		}
	public String generateCode(){
		String label1, label2;
		label1 = Cons.newLabel();
		label2 = Cons.newLabel();
		String code=label1+":";
		Cons.code.add(new String(label2+":"));
		code+=exp.generateCode();
		code+=Cons.jmp("JNE", Cons.ax, label2);
		code+=stmt.generateCode();
		code+=Cons.jmp("jmp", "", label1);
		code+=label2+":";
		Cons.code.add(new String(label2+":"));
		return code;
		}
}

class Return_stmt implements Base
{
	private Expression exp;
	public Return_stmt(){
		}
	public Return_stmt(Expression e){
		exp = e;
		}
	public String generateCode(){
		String code="";
		if(exp!=null)
			code+=exp.generateCode();
		code+=Cons.pop(Cons.pc);
		return code;
		}
	
}

class Var implements Base
{
	private String name;
	private Expression exp;
	public Var(String name){
		this.name = name;
		}
	public Var(String name, Expression e){
		this.name = name;
		exp = e;
		}
	public String generateCode(){
		String code="";
//		System.out.println("lookup "+name);
//		System.out.println(Cons.table.getLevel());
		TableObject to = Cons.table.lookUpObject(name, Cons.table.getLevel());
		if(exp!=null){
			code +=  exp.generateCode();
			code += Cons.move(Cons.si, Cons.ax);
        	}
        else{
        	if(to!=null){
        		if(to.isGlobal){
        			code += Cons.ldc(Cons.bx, to.addr, Cons.ax);
        			}
        		else{
        			code += Cons.move(Cons.bx, Cons.bp);
        			code += Cons.ldc(Cons.tr, to.addr, Cons.ax);
        			code += Cons.add(Cons.bx, Cons.bx, Cons.tr); 
        			code += Cons.ldc(Cons.si, 0, Cons.ax); 
        			}
        		}
        	else{
        		Cons.error("Variable not found");
        		return null;
        		}
        	}
        // dMem[bx+si] is the var address
        return code;
		}
	
	public String getName(){
		return name;
		}
}

class Simple_expression implements Base
{
	private Additive_expression add_exp1, add_exp2;
	private int relop;
	private String label;
	public Simple_expression(Additive_expression a1, int relop, Additive_expression a2){
		add_exp1 = a1;
		add_exp2 = a2;
		this.relop=relop;
		label=null;
		}
	public Simple_expression(Additive_expression a1){
		add_exp1 = a1;
		}
	public String getLabel(){
		return label;
		}
	public String generateCode(){
		String code="";
		if(add_exp2!=null){
			code+=add_exp2.generateCode();
			code+=Cons.move(Cons.cx, Cons.ax);
			code+=add_exp1.generateCode();
			code+=Cons.sub(Cons.ax, Cons.ax, Cons.cx);
			label = Cons.newLabel();
			if(relop==sym.LT){
				code+=Cons.jmp("JLT", Cons.ax, label);
				}
			else if(relop==sym.GT){
				code+=Cons.jmp("JGT", Cons.ax, label);
				}
			else if(relop==sym.EQEQ){
				code+=Cons.jmp("JEQ", Cons.ax, label);
				}
			else if(relop==sym.LTEQ){
				code+=Cons.jmp("JLE", Cons.ax, label);
				}
			else if(relop==sym.GTEQ){
				code+=Cons.jmp("JGE", Cons.ax, label);
				}
			else if(relop==sym.NOTEQ){
				code+=Cons.jmp("JNE", Cons.ax, label);
				}
			code+=Cons.ldc(Cons.ax, 0, Cons.ax);
			code+=label+":";
			Cons.code.add(new String(label+":"));
			code+=Cons.ldc(Cons.ax, 1, Cons.ax);
			return code;
			}
		else
			return add_exp1.generateCode();
		}
	public int isVar(){
		return add_exp1.isVar();
		}
}

class Additive_expression implements Base
{
	private Additive_expression add_exp;
	private int op;
	private Term t;
	
	public Additive_expression(Additive_expression a, int op, Term t){
		add_exp = a;
		this.op = op;
		this.t = t;
		}
	public Additive_expression(Term t){
		this.t = t;
		}
	public int isVar(){
		return t.isVar();
		}

	public String generateCode() {
		String code;
		code=t.generateCode();
		if(add_exp!=null){
			code+=Cons.move(Cons.cx, Cons.ax);
			code+=add_exp.generateCode();
			if(op==sym.PLUS)
				code+=Cons.add(Cons.ax, Cons.ax, Cons.cx);
			else if(op==sym.MINUS)
				code+=Cons.sub(Cons.ax, Cons.ax, Cons.cx);
			}
		return code;
	}
}

class Term implements Base{
	private Factor f;
	private Term t;
	private int mulop;
	public Term(Factor f){
		this.f = f;
		}
	
	public Term(Term t, int mulop, Factor f){
		this.t = t;
		this.mulop = mulop;
		this.f = f;
		}
	public int isVar(){
		return f.isVar();
		}
	public String generateCode(){
		String code;
		code=f.generateCode();
		if(t!=null){
			code+=Cons.move(Cons.cx, Cons.ax);
			code+=t.generateCode();
			if(mulop==sym.TIMES){
				code+=Cons.mul(Cons.ax, Cons.ax, Cons.cx);
				}
			else if(mulop==sym.DIVIDE){
				code+=Cons.div(Cons.ax, Cons.ax, Cons.cx);
				}
			}
		return code;
		}
}

class Factor implements Base{
	private Expression exp;
	private Var var;
	private Call call;
	private int num;
	public Factor(){}
	public Factor(Expression e){
		exp = e;
		}
	public Factor(Var v){
		var = v;
		}
	public Factor(Call c){
		call = c;
		}
	public Factor(int n){
		num = n;
		}
	public String generateCode(){
		if(exp!=null)
			return exp.generateCode();
		else if(var!=null)
			return var.generateCode();
		else if(call!=null)
			return call.generateCode();
		else return Cons.ldc(Cons.ax, num, Cons.ax);
		}
	public int isVar(){
		if(var!=null){
			TableObject to;
			to=Cons.table.lookUpObject(var.getName(), Cons.table.getLevel());
			if(to!=null)
				return to.size;
			}
		return 0;
		}
}

class Call implements Base
{
	String name;
	Args args;
	public Call(String name, Args args){
		this.name = name;
		this.args = args;
		}
	
	public String generateCode(){
		String code="";
		if(name.equals("input")){
			code+=Cons.in(Cons.ax);
			}
		else if(name.equals("output")){
			code += args.generateCode();
			code +=Cons.out(Cons.ax);
			}
		else{
			code += args.generateCode();
			code +=Cons.push(Cons.pc);
			code += Cons.call(name);
		}
		return code;
		}
}

class Args implements Base{
	Arg_list arg_list;
	public Args(Arg_list list){
		arg_list = list;
		}
	public String generateCode(){
		if (arg_list!=null)
		{
			return arg_list.generateCode();
		}
		return new String();
		}
}

class Arg_list implements Base{
	private ArrayList args;
	public Arg_list(){
		args = new ArrayList();
		}
	public Arg_list(Arg_list arg_list){
		args = new ArrayList();
		args.addAll(arg_list.getArgs());
		}
	public Arg_list(Expression e){
		this();
		args.add(e);
		}
	public ArrayList getArgs(){
		return args;
		}
	public void addArg(Expression e){
		args.add(e);
		}
	public String generateCode(){
		String code = "";
		Iterator e = args.iterator();
		Expression exp;
		while(e.hasNext()){
			exp = (Expression) e.next();
			code += exp.generateCode();
			if(exp.isVar() > 1)
				code += Cons.push(Cons.bx);
			else
				code += Cons.push(Cons.ax);
			}
		return code;
		}
}