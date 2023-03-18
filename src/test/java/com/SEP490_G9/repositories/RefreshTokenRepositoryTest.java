package com.SEP490_G9.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import com.SEP490_G9.configs.TestConfig;
import com.SEP490_G9.entities.Account;
import com.SEP490_G9.entities.RefreshToken;
import com.SEP490_G9.repository.RefreshTokenRepository;
@AutoConfigureTestDatabase(replace = Replace.NONE)
@DataJpaTest
@RunWith(SpringRunner.class)
@Import(TestConfig.class)
class RefreshTokenRepositoryTest {
	@Autowired
	RefreshTokenRepository refreshTokenRepository;
	@Test
	public void testFindByToken() {
		RefreshToken rf = new RefreshToken();
		rf.setToken("abcd");
		rf.setId(1);
		int expectedvalue = 1;
		RefreshToken result = refreshTokenRepository.findByToken("abcd");
		assertThat(result.getId()).isEqualTo(expectedvalue);
	}
	@Test
	public void testDeleteByAccount() {
		Account a = new Account();
		a.setId((long)1);
		boolean beforeDelete = refreshTokenRepository.existsById((long)1);
		assertTrue(beforeDelete);
		refreshTokenRepository.deleteByAccount(a);
		boolean afterDelete = refreshTokenRepository.existsById((long)1);
		assertTrue(!afterDelete);
	}
	@Test
	public void findByAccount() {
		Account a = new Account();
		a.setId((long)1);
		RefreshToken result = refreshTokenRepository.findByAccount(a);
		int expectedvalue = 1;
		assertThat(result.getId()).isEqualTo(expectedvalue);
	}

}
