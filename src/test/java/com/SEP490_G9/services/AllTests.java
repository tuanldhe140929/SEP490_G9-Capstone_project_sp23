package com.SEP490_G9.services;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({ AccountServiceTest.class, CartserviceTest.class, FileIOServiceTest.class, PayoutServiceTest.class,
		PreviewServiceTest.class, ProductFileServiceTest.class, ProductServiceTest.class,
		ProductVersionServiceTest.class, ReportServiceTest.class, SellerServiceTest.class,
		TransactionServiceTest.class, UserServiceTest.class, ViolationServiceTest.class })
public class AllTests {

}
