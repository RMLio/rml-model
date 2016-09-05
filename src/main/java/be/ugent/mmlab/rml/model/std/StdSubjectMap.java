package be.ugent.mmlab.rml.model.std;

import be.ugent.mmlab.rml.model.RDFTerm.AbstractTermMap;
import be.ugent.mmlab.rml.model.RDFTerm.GraphMap;
import be.ugent.mmlab.rml.model.RDFTerm.SubjectMap;
import be.ugent.mmlab.rml.model.RDFTerm.TermType;
import be.ugent.mmlab.rml.model.TriplesMap;
import be.ugent.mmlab.rml.model.termMap.ReferenceMap;
import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.eclipse.rdf4j.model.URI;
import org.eclipse.rdf4j.model.Value;

/**
 *************************************************************************
 *
 * RML - Model : Subject Map Implementation
 *
 * A Subject Map is a Term Map that specifies a rule
 * for generating the subjects of the RDF triples 
 * that will be generated by a Triples Map.
 * 
 * @author mielvandersande, andimou
 *
 ***************************************************************************
 */
public class StdSubjectMap extends AbstractTermMap implements SubjectMap {

	protected Set<URI> classIRIs;
	protected HashSet<GraphMap> graphMaps;
        // Log
        private static final Logger log = 
                LoggerFactory.getLogger(
                StdObjectMap.class.getSimpleName());

	public StdSubjectMap(TriplesMap ownTriplesMap, Value constantValue,
			String stringTemplate, URI termType, String inverseExpression,
			ReferenceMap referenceValue, Set<URI> classIRIs, GraphMap graphMap) {
		// No Literal term type
		// ==> No datatype
		// ==> No specified language tag
		super(constantValue, null, null, stringTemplate, termType,
				inverseExpression, referenceValue, graphMap);
		setClassIRIs(classIRIs);
		//TODO: Remove the following setting GraphMaps
		setGraphMaps(graphMaps);
		setOwnTriplesMap(ownTriplesMap);
	}

        @Override
        public void setOwnTriplesMap(TriplesMap ownTriplesMap) {
            // Update triples map if not contains this subject map
            if (ownTriplesMap != null && ownTriplesMap.getSubjectMap() != null) {
                if (ownTriplesMap.getSubjectMap() != this) {
                    log.error("The own triples map "
                            + "already contains another Subject Map !");
                } else {
                    ownTriplesMap.setSubjectMap(this);
                }
            }
            this.ownTriplesMap = ownTriplesMap;
        }

        @Override
	public void setGraphMaps(Set<GraphMap> graphMaps) {
		this.graphMaps = new HashSet<GraphMap>();
		if (graphMaps != null)
			this.graphMaps.addAll(graphMaps);
	}

        @Override
	public void setClassIRIs(Set<URI> classIRIs2) {
		this.classIRIs = new HashSet<URI>();
		if (classIRIs2 != null) {
			checkClassIRIs(classIRIs);
			classIRIs.addAll(classIRIs2);
		}
	}

	private void checkClassIRIs(Set<URI> classIRIs2) {
            // The values of the rr:class property must be IRIs.
            for (URI classIRI : classIRIs) {
                //TODO: Add proper URL chekc
                /*if (!StdIriRdfTerm.isValidURI(classIRI.stringValue())) {
                    log.error(
                            "[AbstractTermMap:checkClassIRIs] Not a valid URI : "
                            + classIRI);
                }*/
            }
        }

        @Override
	public Set<URI> getClassIRIs() {
		return classIRIs;
	}

        @Override
        protected void checkSpecificTermType(TermType tt) {
            // If the term map is a subject map: rr:IRI or rr:BlankNode
            if ((tt != TermType.IRI) && (tt != TermType.BLANK_NODE)) {
                log.error("Invalid Structure "
                        + "If the term map is a "
                        + "subject map: only rr:IRI or rr:BlankNode is required");
            }
        }

        @Override
	protected void checkConstantValue(Value constantValue) {
            // If the constant-valued term map is a subject map then its constant
            // value must be an IRI.
            //TODO: Add proper URL check
            /*if (!StdIriRdfTerm.isValidURI(constantValue.stringValue())) {
                log.error("Data Error "
                        + "[StdSubjectMap:checkConstantValue] Not a valid URI : "
                        + constantValue);
            }*/
        }

        @Override
	public Set<GraphMap> getGraphMaps() {
		return graphMaps;
	}

        @Override
	public TriplesMap getOwnTriplesMap() {
		return ownTriplesMap;
	}

        @Override
	public String toString() {
		String result = super.toString() 
                        + " [StdSubjectMap : classIRIs = [";
		for (URI uri : classIRIs)
			result += uri.getLocalName() + ",";
		result += "], graphMaps = [";
		for (GraphMap graphMap : graphMaps)
			result += graphMap + ",";
		result += "]]";
		return result;
	}

}
