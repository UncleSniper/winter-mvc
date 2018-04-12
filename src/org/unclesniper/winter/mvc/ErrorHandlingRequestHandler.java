package org.unclesniper.winter.mvc;

import java.io.IOException;
import org.unclesniper.winter.mvc.util.SubtypeMap;
import org.unclesniper.winter.mvc.dispatch.NoRouteForRequestException;

public class ErrorHandlingRequestHandler<ParameterT> implements ParameterizedRequestHandler<ParameterT> {

	private ParameterizedRequestHandler<? super ParameterT> requestHandler;

	private RequestParameterMerger<? super ParameterT, ? super ErrorContext> errorContextMerger;

	private final SubtypeMap<Throwable, ParameterizedRequestHandler<? super ParameterT>> errorHandlers
			= new SubtypeMap<Throwable, ParameterizedRequestHandler<? super ParameterT>>();

	public ErrorHandlingRequestHandler() {}

	public ErrorHandlingRequestHandler(ParameterizedRequestHandler<? super ParameterT> requestHandler) {
		this.requestHandler = requestHandler;
	}

	public ErrorHandlingRequestHandler(ParameterizedRequestHandler<? super ParameterT> requestHandler,
			RequestParameterMerger<? super ParameterT, ? super ErrorContext> errorContextMerger) {
		this.requestHandler = requestHandler;
		this.errorContextMerger = errorContextMerger;
	}

	public ParameterizedRequestHandler<? super ParameterT> getRequestHandler() {
		return requestHandler;
	}

	public void setRequestHandler(ParameterizedRequestHandler<? super ParameterT> requestHandler) {
		this.requestHandler = requestHandler;
	}

	public void setRequestHandler(RequestHandler requestHandler) {
		this.requestHandler = requestHandler == null ? null : ParameterizedRequestHandler.ignore(requestHandler);
	}

	public RequestParameterMerger<? super ParameterT, ? super ErrorContext> getErrorContextMerger() {
		return errorContextMerger;
	}

	public void setErrorContextMerger(RequestParameterMerger<? super ParameterT, ? super ErrorContext>
			errorContextMerger) {
		this.errorContextMerger = errorContextMerger;
	}

	public void putErrorHandler(Class<? extends Throwable> errorType,
			ParameterizedRequestHandler<? super ParameterT> errorHandler) {
		errorHandlers.put(errorType, errorHandler);
	}

	public void handleRequest(HTTPRequest request, HTTPResponse response, ParameterT parameter)
			throws IOException, HTTPServiceException {
		Throwable error;
		IOException ioException;
		HTTPServiceException httpServiceException;
		RuntimeException runtimeException;
		try {
			if(requestHandler == null)
				throw new NoRouteForRequestException(request.getMethod(), request.getPath());
			requestHandler.handleRequest(request, response, parameter);
			return;
		}
		catch(IOException ioe) {
			error = ioe;
			ioException = ioe;
			httpServiceException = null;
			runtimeException = null;
		}
		catch(HTTPServiceException hse) {
			error = hse;
			ioException = null;
			httpServiceException = hse;
			runtimeException = null;
		}
		catch(RuntimeException rte) {
			error = rte;
			ioException = null;
			httpServiceException = null;
			runtimeException = rte;
		}
		ParameterizedRequestHandler<? super ParameterT> errorHandler = errorHandlers.get(error.getClass());
		if(errorHandler == null) {
			if(ioException != null)
				throw ioException;
			if(httpServiceException != null)
				throw httpServiceException;
			throw runtimeException;
		}
		ErrorContext context = new ErrorContext(error, request);
		if(errorContextMerger != null)
			errorContextMerger.mergeRequestParameter(parameter, context);
		errorHandler.handleRequest(request, response, parameter);
	}

}
