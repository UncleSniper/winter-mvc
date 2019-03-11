package org.unclesniper.winter.mvc.lens;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Collections;

public class ExtractParameterSequenceInto<DestinationT> implements ExtractParameterInto<DestinationT> {

	public interface Continue {

		boolean hasMoreElements(ParameterSource parameters);

	}

	private static class AdvancingParameterSource implements ParameterSource {

		private interface CachedParameter {

			String getCurrentElement();

			void advance();

		}

		private class IteratorParameter implements CachedParameter {

			private Iterator<String> iterator;

			private String currentElement;

			public IteratorParameter(Iterator<String> iterator) {
				this.iterator = iterator;
				currentElement = iterator.next();
			}

			public String getCurrentElement() {
				return currentElement;
			}

			public void advance() {
				if(iterator == null)
					return;
				if(iterator.hasNext())
					currentElement = iterator.next();
				else {
					iterator = null;
					currentElement = null;
				}
			}

		}

		private static class ExceededParameter implements CachedParameter {

			private static final CachedParameter instance = new ExceededParameter();

			public ExceededParameter() {}

			public String getCurrentElement() {
				return null;
			}

			public void advance() {}

		}

		private final ParameterSource slave;

		private final Map<String, CachedParameter> cachedParameters = new HashMap<String, CachedParameter>();

		private int currentIndex;

		public AdvancingParameterSource(ParameterSource slave) {
			this.slave = slave;
		}

		@Override
		public String getParameter(String name) {
			CachedParameter cp = cachedParameters.get(name);
			if(cp == null) {
				List<String> values = slave.getParameters(name);
				if(values == null || currentIndex >= values.size())
					cp = ExceededParameter.instance;
				else
					cp = new IteratorParameter(values.listIterator(currentIndex));
			}
			return cp.getCurrentElement();
		}

		@Override
		public List<String> getParameters(String name) {
			String value = getParameter(name);
			if(value == null)
				return Collections.emptyList();
			List<String> list = new LinkedList<String>();
			list.add(value);
			return list;
		}

		public void advance() {
			for(CachedParameter entry : cachedParameters.values())
				entry.advance();
			++currentIndex;
		}

	}

	private Continue predicate;

	private ExtractParameterInto<? super DestinationT> element;

	public ExtractParameterSequenceInto(Continue predicate, ExtractParameterInto<? super DestinationT> element) {
		this.predicate = predicate;
		this.element = element;
	}

	public ExtractParameterSequenceInto(String leadParameter,
			ExtractParameterInto<? super DestinationT> element) {
		this(parameters -> parameters.getParameter(leadParameter) != null, element);
	}

	public Continue getPredicate() {
		return predicate;
	}

	public void setPredicate(Continue predicate) {
		this.predicate = predicate;
	}

	public ExtractParameterInto<? super DestinationT> getElement() {
		return element;
	}

	public void setElement(ExtractParameterInto<? super DestinationT> element) {
		this.element = element;
	}

	@Override
	public void extractParameterInto(ParameterSource parameters, DestinationT destination) {
		final AdvancingParameterSource aps = new AdvancingParameterSource(parameters);
		while(predicate.hasMoreElements(aps)) {
			element.extractParameterInto(aps, destination);
			aps.advance();
		}
	}

}
