package com.jc.archive;

import com.jc.foundation.exception.CustomException;

public class ArchiveException extends CustomException {

	public ArchiveException() {
	}

	public ArchiveException(String msg) {
		super(msg);
	}

	public ArchiveException(Throwable e) {
		super(e);
	}

	public ArchiveException(String msg, Throwable e) {
		super(msg, e);
	}

}
