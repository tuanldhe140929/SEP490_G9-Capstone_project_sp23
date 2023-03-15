package com.SEP490_G9.repositories;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({ CategoryRepositoryTest.class , TagRepositoryTest.class})
public class RepositoryTests {

}
