package edu.ucsc.cs.netEvo;

import static org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants.*;

import org.eclipse.jdt.internal.compiler.CompilationResult;
import org.eclipse.jdt.internal.compiler.DefaultErrorHandlingPolicies;
import org.eclipse.jdt.internal.compiler.ast.CompilationUnitDeclaration;
import org.eclipse.jdt.internal.compiler.batch.CompilationUnit;
import org.eclipse.jdt.internal.compiler.env.ICompilationUnit;
import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.jdt.internal.compiler.parser.Parser;
import org.eclipse.jdt.internal.compiler.problem.DefaultProblemFactory;
import org.eclipse.jdt.internal.compiler.problem.ProblemReporter;


public class Utils {
	public static CompilationUnitDeclaration compile(String sourceCode, String fileName) {
		long[] versions = {JDK1_8, JDK1_7, JDK1_6, JDK1_5, JDK1_4, JDK1_3, JDK1_2, JDK1_1};
        CompilerOptions options = new CompilerOptions();
        options.docCommentSupport = true;
		for (long v : versions) {
	        options.complianceLevel = v;
	        options.sourceLevel = v;
	        options.targetJDK = v;
	        Parser parser = new Parser(
	        		new ProblemReporter(
		                DefaultErrorHandlingPolicies.proceedWithAllProblems(),
		                options,
		                new DefaultProblemFactory()),
		            false);
	        ICompilationUnit cu = new CompilationUnit(sourceCode.toCharArray(), fileName, null);
	        CompilationResult compilationResult = new CompilationResult(cu, 0, 0, options.maxProblemsPerUnit);
	        CompilationUnitDeclaration ast = parser.parse(cu, compilationResult);
	        if (!compilationResult.hasSyntaxError) {
	        	return ast;
	        }
		}
		return null;
	}
}
