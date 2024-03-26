package com.vitcheu.prop;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import jakarta.annotation.Resource;

@SpringBootTest
@ActiveProfiles({ "mysql", "production" })
public class JdbcTest {

  @Resource
  JdbcTemplate jdbcTemplate;

  @Test
  public void test() {
    List<Map<String, Object>> list = jdbcTemplate.queryForList(
      "SELECT * FROM  test_t;"
    );
    System.out.println(list);
  }
}
