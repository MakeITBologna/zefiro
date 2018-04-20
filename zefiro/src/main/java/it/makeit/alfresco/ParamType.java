package it.makeit.alfresco;

import java.util.Date;

import org.apache.chemistry.opencmis.client.api.QueryStatement;
import org.apache.chemistry.opencmis.client.runtime.ObjectIdImpl;

enum ParamType {
	TYPE {

		@Override
		public int addTerm(QueryStatement pQS, String pDocTypeId, CmisQueryPredicate<?> pTerm, int pIdx) {
			pQS.setType(pIdx, (String) pTerm.getValue());
			return ++pIdx;
		}
	},
	PROPERTY {

		@Override
		public int addTerm(QueryStatement pQS, String pDocTypeId, CmisQueryPredicate<?> pTerm, int pIdx) {
			pQS.setProperty(pIdx, pDocTypeId, pTerm.getPropertyId());
			return ++pIdx;
		}
	},
	STRING {

		@Override
		public int addTerm(QueryStatement pQS, String pDocTypeId, CmisQueryPredicate<?> pTerm, int pIdx) {
			pQS.setProperty(pIdx, pDocTypeId, pTerm.getPropertyId());
			pQS.setString(++pIdx, (String) pTerm.getValue());
			return ++pIdx;
		}
	},
	NUMBER {

		@Override
		public int addTerm(QueryStatement pQS, String pDocTypeId, CmisQueryPredicate<?> pTerm, int pIdx) {
			String lPropertyId = pTerm.getPropertyId();
			Number lFrom = (Number) pTerm.getValueFrom();
			Number lTo = (Number) pTerm.getValueTo();

			if (lFrom != null) {
				pQS.setProperty(pIdx, pDocTypeId, lPropertyId);
				pQS.setNumber(++pIdx, (Number) pTerm.getValueFrom());
			}
			if (lFrom != null && lTo != null) {
				pIdx++;
			}
			if (lTo != null) {
				pQS.setProperty(pIdx, pDocTypeId, lPropertyId);
				pQS.setNumber(++pIdx, (Number) pTerm.getValueTo());
			}
			return ++pIdx;
		}
	},
	BOOLEAN {

		@Override
		public int addTerm(QueryStatement pQS, String pDocTypeId, CmisQueryPredicate<?> pTerm, int pIdx) {
			pQS.setProperty(pIdx, pDocTypeId, pTerm.getPropertyId());
			pQS.setBoolean(++pIdx, (Boolean) pTerm.getValue());
			return ++pIdx;
		}

	},
	DATETIME {

		@Override
		public int addTerm(QueryStatement pQS, String pDocTypeId, CmisQueryPredicate<?> pTerm, int pIdx) {
			String lPropertyId = pTerm.getPropertyId();
			Date lFrom = (Date) pTerm.getValueFrom();
			Date lTo = (Date) pTerm.getValueTo();

			if (lFrom != null) {
				pQS.setProperty(pIdx, pDocTypeId, lPropertyId);
				pQS.setDateTimeTimestamp(++pIdx, lFrom);
			}
			if (lFrom != null && lTo != null) {
				pIdx++;
			}
			if (lTo != null) {
				pQS.setProperty(pIdx, pDocTypeId, lPropertyId);
				pQS.setDateTimeTimestamp(++pIdx, lTo);
			}
			return ++pIdx;
		}
	},
	ID {

		@Override
		public int addTerm(QueryStatement pQS, String pDocTypeId, CmisQueryPredicate<?> pTerm, int pIdx) {
			pQS.setProperty(pIdx, pDocTypeId, pTerm.getPropertyId());
			pQS.setId(++pIdx, new ObjectIdImpl((String) pTerm.getValue()));
			return ++pIdx;
		}
	},
	// URI,
	// URL,
	IN_FOLDER {

		@Override
		public int addTerm(QueryStatement pQS, String pDocTypeId, CmisQueryPredicate<?> pTerm, int pIdx) {
			pQS.setId(pIdx, new ObjectIdImpl((String) pTerm.getValue()));
			return ++pIdx;
		}
	},
	IN_TREE {

		@Override
		public int addTerm(QueryStatement pQS, String pDocTypeId, CmisQueryPredicate<?> pTerm, int pIdx) {
			pQS.setId(pIdx, new ObjectIdImpl((String) pTerm.getValue()));
			return ++pIdx;
		}
	},
	CONTAINS {

		@Override
		public int addTerm(QueryStatement pQS, String pDocTypeId, CmisQueryPredicate<?> pTerm, int pIdx) {
			pQS.setStringContains(pIdx, (String) pTerm.getValue());
			return ++pIdx;
		}
	};

	public abstract int addTerm(QueryStatement pQS, String pDocTypeId, CmisQueryPredicate<?> pTerm, int pIdx);
}
