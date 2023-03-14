package com.SEP490_G9.repositories;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({ ProductDetailsRepositoryTest.class, ProductRepositoryTest.class })
public class RepositoryTests {

}
