package edu.ucsc.cs.netEvo;

public class CodeEntity {
	String qualifiedName;
	int fileId;

	CodeEntity(String name, int fileId) {
		this.qualifiedName = name;
		this.fileId = fileId;
	}
	
	@Override
	public String toString() {
		return qualifiedName;
	}
}

