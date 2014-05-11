package edu.ucsc.cs.netEvo;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.internal.compiler.ASTVisitor;
import org.eclipse.jdt.internal.compiler.ast.CompilationUnitDeclaration;
import org.eclipse.jdt.internal.compiler.ast.TypeDeclaration;
import org.eclipse.jdt.internal.compiler.lookup.ClassScope;
import org.eclipse.jdt.internal.compiler.lookup.CompilationUnitScope;

public class SourceFileAnalyzer extends ASTVisitor {
	List<CodeEntity> vertices = new ArrayList<>();
	List<Dependency> edges = new ArrayList<>();
	String packageName;
	int fileId;

	public SourceFileAnalyzer(int fileId) {
		this.fileId = fileId;
	}

	@Override
	public boolean visit(CompilationUnitDeclaration compilationUnitDeclaration,
			CompilationUnitScope scope) {
		packageName = compilationUnitDeclaration.currentPackage.toString();
		return true; // do nothing by default, keep traversing
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
		String name = packageName + '.' + new String(typeDeclaration.name);
		vertices.add(new CodeEntity(name, fileId));
		return true;
	}
}
