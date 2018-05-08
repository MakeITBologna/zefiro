package it.makeit.alfresco.workflow.model;

public class Candidate {
	private String candidateType;
	private String candidateId;

	public String getCandidateType() {
		return candidateType;
	}

	public String getCandidateId() {
		return candidateId;
	}

	public void setCandidateType(String candidateType) {
		this.candidateType = candidateType;
	}

	public void setCandidateId(String candidateId) {
		this.candidateId = candidateId;
	}
}
