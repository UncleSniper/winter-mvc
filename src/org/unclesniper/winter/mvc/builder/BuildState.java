package org.unclesniper.winter.mvc.builder;

import java.util.Set;
import java.util.List;
import java.util.HashSet;
import java.util.LinkedList;
import org.unclesniper.winter.mvc.HTTPVerb;
import org.unclesniper.winter.mvc.util.Transform;
import org.unclesniper.winter.mvc.dispatch.PathMatcher;
import org.unclesniper.winter.mvc.dispatch.DispatchRule;
import org.unclesniper.winter.mvc.dispatch.StringPathMatcher;
import org.unclesniper.winter.mvc.ParameterizedRequestHandler;
import org.unclesniper.winter.mvc.DispatchModelRequestHandler;

public final class BuildState<PathKeyT, RequestParameterT> {

	private Transform<? super ParameterizedRequestHandler<? super RequestParameterT>,
			? extends ParameterizedRequestHandler<? super RequestParameterT>> handlerTransform;

	private int verbMask;

	private final List<PathMatcher<PathKeyT>> pathHead = new LinkedList<PathMatcher<PathKeyT>>();

	private final List<PathMatcher<PathKeyT>> pathTail = new LinkedList<PathMatcher<PathKeyT>>();

	private final Set<String> contentTypes = new HashSet<String>();

	private final List<DispatchRule<PathKeyT, RequestParameterT>> rules
			= new LinkedList<DispatchRule<PathKeyT, RequestParameterT>>();

	public BuildState(Transform<? super ParameterizedRequestHandler<? super RequestParameterT>,
			? extends ParameterizedRequestHandler<? super RequestParameterT>> handlerTransform) {
		this.handlerTransform = handlerTransform;
	}

	public void clearVerbs() {
		verbMask = 0;
	}

	public void addVerbs(HTTPVerb... verbs) {
		for(HTTPVerb verb : verbs)
			verbMask |= 1 << verb.ordinal();
	}

	public void clearHead() {
		pathHead.clear();
	}

	@SuppressWarnings("unchecked")
	public void addHead(PathMatcher<PathKeyT>... path) {
		for(PathMatcher<PathKeyT> matcher : path)
			pathHead.add(matcher);
	}

	public void addHead(String... path) {
		for(String segment : path)
			pathHead.add(segment == null ? null : new StringPathMatcher<PathKeyT>(segment));
	}

	public void clearTail() {
		pathTail.clear();
	}

	@SuppressWarnings("unchecked")
	public void addTail(PathMatcher<PathKeyT>... path) {
		for(PathMatcher<PathKeyT> matcher : path)
			pathTail.add(matcher);
	}

	public void addTail(String... path) {
		for(String segment : path)
			pathTail.add(segment == null ? null : new StringPathMatcher<PathKeyT>(segment));
	}

	public void clearContentTypes() {
		contentTypes.clear();
	}

	public void addContentTypes(String... types) {
		for(String type : types)
			contentTypes.add(type);
	}

	public void emitRule(ParameterizedRequestHandler<? super RequestParameterT> handler) {
		DispatchRule<PathKeyT, RequestParameterT> rule
				= new DispatchRule<PathKeyT, RequestParameterT>(verbMask == 0
				? 1 << HTTPVerb.GET.ordinal() : verbMask, handlerTransform == null
				? handler : handlerTransform.transform(handler));
		rule.addPathMatchers(pathHead);
		rule.addPathMatchers(pathTail);
		if(contentTypes.isEmpty())
			rule.addContentType(null);
		else
			rule.addContentTypes(contentTypes);
		rules.add(rule);
	}

	public void putRules(DispatchModelRequestHandler<PathKeyT, RequestParameterT> dispatcher) {
		for(DispatchRule<PathKeyT, RequestParameterT> rule : rules)
			dispatcher.addRule(rule);
	}

}
