package edu.ucsc.cs.netEvo;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.internal.compiler.ASTVisitor;
import org.eclipse.jdt.internal.compiler.ast.AbstractMethodDeclaration;
import org.eclipse.jdt.internal.compiler.ast.CompilationUnitDeclaration;
import org.eclipse.jdt.internal.compiler.ast.ImportReference;
import org.eclipse.jdt.internal.compiler.ast.TypeDeclaration;
import org.eclipse.jdt.internal.compiler.lookup.ClassScope;
import org.eclipse.jdt.internal.compiler.lookup.CompilationUnitScope;
import org.eclipse.jdt.internal.compiler.lookup.MethodScope;
import org.eclipse.jdt.internal.compiler.lookup.Scope;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

public class SourceFileAnalyzer extends ASTVisitor {
	List<CodeEntity> vertices = new ArrayList<>();
	List<Dependency> edges = new ArrayList<>();
	int fileId;

	public SourceFileAnalyzer(int fileId) {
		this.fileId = fileId;
	}
	
	static String getQualifiedName(Scope scope) {
		ArrayList<String> parts = new ArrayList<>();
		while (scope != null) {
			if (scope instanceof CompilationUnitScope) {
				ImportReference currentPackage = ((CompilationUnitScope)scope).referenceContext.currentPackage;
				if (currentPackage != null) {
					parts.add(currentPackage.toString());					
				}
			} else if (scope instanceof ClassScope) {
				char[] name = ((ClassScope)scope).referenceContext.name;
				if (name != null) {
					parts.add(new String(name));					
				}
			} else if (scope instanceof MethodScope) {
				AbstractMethodDeclaration method = ((MethodScope)scope).referenceMethod();
				if (method != null) {
					parts.add(new String(method.selector));					
				}
			}
			scope = scope.parent;
		}
		return Joiner.on(',').join(Lists.reverse(parts));
	}

	@Override
	public boolean visit(TypeDeclaration memberTypeDeclaration, ClassScope scope) {
		CompilationUnitDeclaration cu = memberTypeDeclaration
				.getCompilationUnitDeclaration();
		return true;
	}

	@Override
	public boolean visit(TypeDeclaration typeDeclaration,
			CompilationUnitScope scope) {
		String name = getQualifiedName(scope) + '.' + new String(typeDeclaration.name);
		vertices.add(new CodeEntity(name, fileId));
		return true;
	}
}
