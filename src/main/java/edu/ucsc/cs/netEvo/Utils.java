package edu.ucsc.cs.netEvo;

import static org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;

import org.eclipse.jdt.internal.compiler.CompilationResult;
import org.eclipse.jdt.internal.compiler.DefaultErrorHandlingPolicies;
import org.eclipse.jdt.internal.compiler.ast.CompilationUnitDeclaration;
import org.eclipse.jdt.internal.compiler.batch.CompilationUnit;
import org.eclipse.jdt.internal.compiler.env.ICompilationUnit;
import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.jdt.internal.compiler.lookup.CompilationUnitScope;
import org.eclipse.jdt.internal.compiler.lookup.LookupEnvironment;
import org.eclipse.jdt.internal.compiler.parser.Parser;
import org.eclipse.jdt.internal.compiler.problem.DefaultProblemFactory;
import org.eclipse.jdt.internal.compiler.problem.ProblemReporter;

import com.google.common.collect.Lists;


public class Utils {
	static Properties config;
	static Set<String> javaLang;
	
	public static CompilationUnitDeclaration parse(String sourceCode, String fileName) {
		long[] versions = {JDK1_8, JDK1_7, JDK1_6, JDK1_5, JDK1_4, JDK1_3, JDK1_2, JDK1_1};
        CompilerOptions options = new CompilerOptions();
        options.docCommentSupport = true;
		for (long v : versions) {
	        options.complianceLevel = v;
	        options.sourceLevel = v;
	        options.targetJDK = v;
	        ProblemReporter problemReporter = new ProblemReporter(
			    DefaultErrorHandlingPolicies.proceedWithAllProblems(),
			    options,
			    new DefaultProblemFactory());
			Parser parser = new Parser(
	        		problemReporter,
		            false);
	        ICompilationUnit cu = new CompilationUnit(sourceCode.toCharArray(), fileName, null);
	        CompilationResult compilationResult = new CompilationResult(cu, 0, 0, options.maxProblemsPerUnit);
	        CompilationUnitDeclaration ast = parser.parse(cu, compilationResult);
	        if (ast.scope == null) {
	        	ast.scope = new CompilationUnitScope(ast, new LookupEnvironment(null, options, problemReporter, null));
	        }
	        if (!compilationResult.hasSyntaxError) {
	        	return ast;
	        } else {
	        	Logger.getLogger("Utils.parse").warning(compilationResult.toString());
	        }
		}
		return null;
	}
	
	public static String[] toStringArray(char[][] tokens) {
		String parts[] = new String[tokens.length];
		for (int i = 0; i < tokens.length; i++) {
			parts[i] = new String(tokens[i]);
		}
		return parts;
	}

	public static Properties getConfig() {
		if (config == null) {
			config = new Properties();
			try {
				FileInputStream fis = new FileInputStream("config.properties");
				config.load(fis);
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
		return config;
	}
	
	public static Set<String> javaLangClasses() {
		if (javaLang == null) {			
			javaLang = new HashSet<>(Lists.newArrayList(
				"Appendable",
				"AutoCloseable",
				"CharSequence",
				"Cloneable",
				"Comparable",
				"Iterable",
				"Readable",
				"Runnable",
				"Thread.UncaughtExceptionHandler",
				"Boolean",
				"Byte",
				"Character",
				"Character.Subset",
				"Character.UnicodeBlock",
				"Class",
				"ClassLoader",
				"ClassValue",
				"Compiler",
				"Double",
				"Enum",
				"Float",
				"InheritableThreadLocal",
				"Integer",
				"Long",
				"Math",
				"Number",
				"Object",
				"Package",
				"Process",
				"ProcessBuilder",
				"ProcessBuilder.Redirect",
				"Runtime",
				"RuntimePermission",
				"SecurityManager",
				"Short",
				"StackTraceElement",
				"StrictMath",
				"String",
				"StringBuffer",
				"StringBuilder",
				"System",
				"Thread",
				"ThreadGroup",
				"ThreadLocal",
				"Throwable",
				"Void",
				"Character.UnicodeScript",
				"ProcessBuilder.Redirect.Type",
				"Thread.State",
				"ArithmeticException",
				"ArrayIndexOutOfBoundsException",
				"ArrayStoreException",
				"ClassCastException",
				"ClassNotFoundException",
				"CloneNotSupportedException",
				"EnumConstantNotPresentException",
				"Exception",
				"IllegalAccessException",
				"IllegalArgumentException",
				"IllegalMonitorStateException",
				"IllegalStateException",
				"IllegalThreadStateException",
				"IndexOutOfBoundsException",
				"InstantiationException",
				"InterruptedException",
				"NegativeArraySizeException",
				"NoSuchFieldException",
				"NoSuchMethodException",
				"NullPointerException",
				"NumberFormatException",
				"ReflectiveOperationException",
				"RuntimeException",
				"SecurityException",
				"StringIndexOutOfBoundsException",
				"TypeNotPresentException",
				"UnsupportedOperationException",
				"AbstractMethodError",
				"AssertionError",
				"BootstrapMethodError",
				"ClassCircularityError",
				"ClassFormatError",
				"Error",
				"ExceptionInInitializerError",
				"IllegalAccessError",
				"IncompatibleClassChangeError",
				"InstantiationError",
				"InternalError",
				"LinkageError",
				"NoClassDefFoundError",
				"NoSuchFieldError",
				"NoSuchMethodError",
				"OutOfMemoryError",
				"StackOverflowError",
				"ThreadDeath",
				"UnknownError",
				"UnsatisfiedLinkError",
				"UnsupportedClassVersionError",
				"VerifyError",
				"VirtualMachineError",
				"Deprecated",
				"FunctionalInterface",
				"Override",
				"SafeVarargs",
				"SuppressWarnings"
			));
		}
		return javaLang;
	}
}
