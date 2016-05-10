package it.makeit.alfresco;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.chemistry.opencmis.client.api.QueryStatement;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.commons.enums.BaseTypeId;


public class CmisQueryBuilder {

	private StringBuilder sb = new StringBuilder();

	private Session session;

	private String docTypeId;
	private List<String> properties = new LinkedList<String>();
	private List<CmisQueryPredicate<?>> predicates = new LinkedList<CmisQueryPredicate<?>>();

	private boolean built = false;
	private String resolvedQuery;

	public CmisQueryBuilder(Session pSession) {
		this.session = pSession;
	}

	public CmisQueryBuilder selectFrom(String pDocTypeId, String... pProperty) {
		if (pDocTypeId == null) {
			// FIXME (Alessio)
			throw new NullPointerException();
		}

		// SELECT
		sb.append("SELECT ");
		if (pProperty != null && pProperty.length > 0) {

			for (int i = 0; i < pProperty.length; i++) {
				sb.append("?, ");
				this.properties.add(pProperty[i]);
			}
			sb.delete(sb.length() - 2, sb.length());

		} else {
			sb.append("*");
		}

		// FROM
		sb.append(" FROM ? ");
		if (pDocTypeId != null && pDocTypeId.length() > 0) {
			this.docTypeId = pDocTypeId;
		} else {
			this.docTypeId = BaseTypeId.CMIS_DOCUMENT.toString();
		}

		return this;
	}

	private void addClausesList(List<CmisQueryPredicate<?>> pListPredicates) {
		sb.append("(");
		for (CmisQueryPredicate<?> predicate : pListPredicates) {
			this.predicates.add(predicate);

			switch (predicate.getOperator()) {
			case EQ:
				sb.append("? = ?");
				break;
			case LE:
				sb.append("? <= ?");
				break;
			case GE:
				sb.append("? >= ?");
				break;
			case BETWEEN:
				sb.append("(");
				if (predicate.getValueFrom() != null) {
					sb.append("? >= ?");
				}
				if (predicate.getValueFrom() != null && predicate.getValueTo() != null) {
					sb.append(" AND ");
				}
				if (predicate.getValueTo() != null) {
					sb.append("? <= ?");
				}
				sb.append(")");
				break;
			case LIKE:
				sb.append("? LIKE ?");
				break;
			case IN_FOLDER:
				sb.append("IN_FOLDER(?)");
				break;
			case IN_TREE:
				sb.append("IN_TREE(?)");
				break;
			case CONTAINS:
				sb.append("CONTAINS(?)");
				break;
			}
			sb.append(" AND ");
		}
		sb.delete(sb.length() - 5, sb.length());
		sb.append(") ");
	}

	/**
	 * Stabilisce le clausole di {@code WHERE}
	 * 
	 * @param pListPredicates
	 *            La lista dei predicati. Tutti i predicati nella lista saranno valutati in
	 *            {@code AND}.
	 * 
	 * @return L'istanza stessa del {@code CmisQueryBuilder} che si sta configurando, in modo da
	 *         consentire il concatenamento delle operazioni.
	 */
	public CmisQueryBuilder where(List<CmisQueryPredicate<?>> pListPredicates) {
		if (pListPredicates != null && !pListPredicates.isEmpty()) {
			sb.append("WHERE ");
			addClausesList(pListPredicates);
		}
		return this;
	}

	/**
	 * Stabilisce le clausole di {@code WHERE}
	 * 
	 * @param pPredicate
	 *            Uno o più predicati. Tutti i predicati indicati saranno valutati in
	 *            {@code AND}.
	 * 
	 * @return L'istanza stessa del {@code CmisQueryBuilder} che si sta configurando, in modo da
	 *         consentire il concatenamento delle operazioni.
	 */
	public CmisQueryBuilder where(CmisQueryPredicate<?>... pPredicate) {
		return where(Arrays.asList(pPredicate));
	}

	public CmisQueryBuilder and(List<CmisQueryPredicate<?>> pListPredicates) {
		if (pListPredicates != null && !pListPredicates.isEmpty()) {
			sb.append("AND ");
			addClausesList(pListPredicates);
		}
		return this;
	}

	public CmisQueryBuilder and(CmisQueryPredicate<?>... pPredicate) {
		return and(Arrays.asList(pPredicate));
	}

	public CmisQueryBuilder or(List<CmisQueryPredicate<?>> pListPredicates) {
		if (pListPredicates != null && !pListPredicates.isEmpty()) {
			sb.append("OR ");
			addClausesList(pListPredicates);
		}
		return this;
	}

	public CmisQueryBuilder or(CmisQueryPredicate<?>... pPredicate) {
		return or(Arrays.asList(pPredicate));
	}

	public String build() {
		// TODO (Alessio): gestire completamente il flag built
		if (!built) {
			int curIdx = 1;

			QueryStatement lQS = session.createQueryStatement(sb.toString());
			// SELECT
			for (String property : properties) {
				lQS.setProperty(curIdx, docTypeId, property);
				curIdx++;
			}

			// FROM
			lQS.setType(curIdx, docTypeId);
			curIdx++;

			// WHERE
			for (CmisQueryPredicate<?> predicate : predicates) {
				curIdx = predicate.getParamType().addTerm(lQS, docTypeId, predicate, curIdx);
			}

			this.built = true;
			this.resolvedQuery = lQS.toQueryString();
		}

		return this.resolvedQuery;
	}

	public String getWhereConditions() {
		if (!built) {
			build();
		}
		int beginIndex = this.resolvedQuery.indexOf("WHERE") + 6;
		return this.resolvedQuery.substring(beginIndex);
	}

}
