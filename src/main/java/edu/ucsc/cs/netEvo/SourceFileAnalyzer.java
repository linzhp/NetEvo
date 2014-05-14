package edu.ucsc.cs.netEvo;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.internal.compiler.ASTVisitor;
import org.eclipse.jdt.internal.compiler.ast.*;
import org.eclipse.jdt.internal.compiler.lookup.*;

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
				ImportReference currentPackage = ((CompilationUnitScope) scope).referenceContext.currentPackage;
				if (currentPackage != null) {
					parts.add(currentPackage.toString());
				}
			} else if (scope instanceof ClassScope) {
				char[] name = ((ClassScope) scope).referenceContext.name;
				if (name != null) {
					parts.add(new String(name));
				}
			} else if (scope instanceof MethodScope) {
				AbstractMethodDeclaration method = ((MethodScope) scope)
						.referenceMethod();
				if (method != null) {
					parts.add(new String(method.selector));
				}
			}
			scope = scope.parent;
		}
		return Joiner.on('.').join(Lists.reverse(parts));
	}

	@Override
	public boolean visit(TypeDeclaration memberTypeDeclaration, ClassScope scope) {
		memberTypeDeclaration.scope = new ClassScope(scope,
				memberTypeDeclaration);
		vertices.add(new CodeEntity(
				getQualifiedName(memberTypeDeclaration.scope), fileId));
		return true;
	}

	@Override
	public boolean visit(TypeDeclaration typeDeclaration,
			CompilationUnitScope scope) {
		typeDeclaration.scope = new ClassScope(scope, typeDeclaration);
		vertices.add(new CodeEntity(getQualifiedName(typeDeclaration.scope),
				fileId));
		return true;
	}

	@Override
	public boolean visit(MethodDeclaration methodDeclaration, ClassScope scope) {
		methodDeclaration.scope = new MethodScope(scope, methodDeclaration,
				methodDeclaration.isStatic());
		vertices.add(new CodeEntity(getQualifiedName(methodDeclaration.scope),
				fileId));
		return true;
	}

	@Override
	public boolean visit(ImportReference importRef, CompilationUnitScope scope) {
		// TODO build import list
		return true; // do nothing by default, keep traversing
	}

	public boolean visit(
    		SingleTypeReference singleTypeReference,
    		BlockScope scope) {
		edges.add(new Dependency(getQualifiedName(scope), new String(singleTypeReference.token)));
		return true;
	}

}
