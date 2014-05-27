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
		memberTypeDeclaration.initializerScope = new MethodScope(
				memberTypeDeclaration.scope, memberTypeDeclaration, false);
		memberTypeDeclaration.staticInitializerScope = new MethodScope(
				memberTypeDeclaration.scope, memberTypeDeclaration, true);
		vertices.add(new CodeEntity(
				getQualifiedName(memberTypeDeclaration.scope), fileId));
		return true;
	}

	@Override
	public boolean visit(TypeDeclaration typeDeclaration,
			CompilationUnitScope scope) {
		typeDeclaration.scope = new ClassScope(scope, typeDeclaration);
		typeDeclaration.initializerScope = new MethodScope(typeDeclaration.scope, typeDeclaration, false);
		typeDeclaration.staticInitializerScope = new MethodScope(typeDeclaration.scope, typeDeclaration, true);
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

	public boolean visit(ConstructorDeclaration constructorDeclaration,
			ClassScope scope) {
		constructorDeclaration.scope = new MethodScope(scope, constructorDeclaration, false);
		vertices.add(new CodeEntity(
				getQualifiedName(constructorDeclaration.scope), fileId));
		return true;
	}

	public boolean visit(Clinit clinit, ClassScope scope) {
		clinit.scope = new MethodScope(scope, clinit, clinit.isStatic());
		return true; // do nothing by default, keep traversing
	}

	public boolean visit(FieldDeclaration fieldDeclaration, MethodScope scope) {
		String fieldName = getQualifiedName(scope) + "." + new String(fieldDeclaration.name);
		vertices.add(new CodeEntity(
				fieldName, fileId));
		return true;
	}

	@Override
	public boolean visit(ImportReference importRef, CompilationUnitScope scope) {
		// TODO build import list
		return true;
	}

	@Override
	public boolean visit(SingleTypeReference singleTypeReference,
			BlockScope scope) {
		edges.add(new Dependency(getQualifiedName(scope), singleTypeReference
				.toString()));
		return true;
	}

	@Override
	public boolean visit(SingleTypeReference singleTypeReference,
			ClassScope scope) {
		edges.add(new Dependency(getQualifiedName(scope), singleTypeReference
				.toString()));
		return true;
	}

	@Override
	public boolean visit(QualifiedTypeReference qualifiedTypeReference,
			BlockScope scope) {
		edges.add(new Dependency(getQualifiedName(scope),
				qualifiedTypeReference.toString()));
		return true;
	}

	@Override
	public boolean visit(QualifiedTypeReference qualifiedTypeReference,
			ClassScope scope) {
		edges.add(new Dependency(getQualifiedName(scope),
				qualifiedTypeReference.toString()));
		return true;
	}

	@Override
	public boolean visit(QualifiedNameReference qualifiedNameReference,
			BlockScope scope) {
		edges.add(new Dependency(getQualifiedName(scope),
				qualifiedNameReference.toString()));
		return true;
	}

	@Override
	public boolean visit(QualifiedNameReference qualifiedNameReference,
			ClassScope scope) {
		edges.add(new Dependency(getQualifiedName(scope),
				qualifiedNameReference.toString()));
		return true;
	}

	@Override
	public boolean visit(
			ArrayQualifiedTypeReference arrayQualifiedTypeReference,
			BlockScope scope) {
		edges.add(new Dependency(getQualifiedName(scope), Joiner.on('.').join(
				Utils.toStringArray(arrayQualifiedTypeReference.tokens))));
		return true;
	}

	@Override
	public boolean visit(
			ArrayQualifiedTypeReference arrayQualifiedTypeReference,
			ClassScope scope) {
		edges.add(new Dependency(getQualifiedName(scope), Joiner.on('.').join(
				Utils.toStringArray(arrayQualifiedTypeReference.tokens))));
		return true;
	}

	@Override
	public boolean visit(ArrayTypeReference arrayTypeReference, BlockScope scope) {
		edges.add(new Dependency(getQualifiedName(scope), new String(
				arrayTypeReference.token)));
		return true;
	}

	@Override
	public boolean visit(ArrayTypeReference arrayTypeReference, ClassScope scope) {
		edges.add(new Dependency(getQualifiedName(scope), new String(
				arrayTypeReference.token)));
		return true;
	}

	@Override
	public boolean visit(ExplicitConstructorCall explicitConstructor,
			BlockScope scope) {
		// not process for now, postpone until method overloading is considered
		return true;
	}

	@Override
	public boolean visit(FieldReference fieldReference, BlockScope scope) {
		return true; // do nothing by default, keep traversing
	}

	@Override
	public boolean visit(FieldReference fieldReference, ClassScope scope) {
		return true; // do nothing by default, keep traversing
	}

}
