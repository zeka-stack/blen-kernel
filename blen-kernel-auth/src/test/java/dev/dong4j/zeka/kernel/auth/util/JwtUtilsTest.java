package dev.dong4j.zeka.kernel.auth.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import dev.dong4j.zeka.kernel.auth.JwtTokenInfo;
import dev.dong4j.zeka.kernel.auth.constant.AuthConstant;
import dev.dong4j.zeka.kernel.auth.entity.AuthorizationRole;
import dev.dong4j.zeka.kernel.auth.entity.AuthorizationUser;
import dev.dong4j.zeka.kernel.auth.entity.ZekaClaims;
import dev.dong4j.zeka.kernel.auth.enums.UserType;
import dev.dong4j.zeka.kernel.common.util.BeanUtils;
import dev.dong4j.zeka.kernel.common.util.JsonUtils;
import dev.dong4j.zeka.kernel.common.util.StringUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.security.jwt.Jwt;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.2.3
 * @email "mailto:dong4j@gmail.com"
 * @date 2020.01.27 14:53
 * @since 1.0.0
 */
@Slf4j
class JwtUtilsTest {
    /** Object mapper */
    private final ObjectMapper objectMapper = JsonUtils.getCopyMapper();
    /** Key */
    private final String key = "";
    /** Token */
    private String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9" +
        ".eyJ1c2VyX25hbWUiOiJhZG1pbiIsInNjb3BlIjpbImFsbCJdLCJyb2xlcyI6W3siaWQiOjE2LCJyb2xlTmFtZSI6Iui2hee6p" +
        "-euoeeQhuWRmCJ9XSwiZXhwIjoxNTgyNDQyNjI0LCJ1c2VyIjp7ImlkIjoxLCJtb2JpbGUiOiIxODcwMDAwMDAwMCIsInVzZXJuYW1lIjoiYWRtaW4iLCJ1c2VySWRlbnRpdHkiOjB9LCJhdXRob3JpdGllcyI6WyLotoXnuqfnrqHnkIblkZgiXSwianRpIjoiZjQ0ZWQzZWMtMDIyMS00M2JiLWE5MDEtMzQzMzEzMmM3MjhiIiwiY2xpZW50X2lkIjoiZmtoX2Rhc2hib2FyZCJ9.rIH2TeLD03ysAY8yUvx3GBlgHsIL3LveeOLUK6Hnyrs";

    /**
     * token 过期
     *
     * @since 1.0.0
     */
    @Test
    void test1() {
        assertThrows(ExpiredJwtException.class,
            () -> {
                JwtUtils.getClaims("1234567890", this.token);
            });

    }

    /**
     * token 签名错误
     *
     * @since 1.0.0
     */
    @Test
    void test2() {
        assertThrows(SignatureException.class,
            () -> {
                JwtUtils.getClaims("123456789", this.token);
            });

    }

    /**
     * token 非法
     *
     * @since 1.0.0
     */
    @Test
    void test3() {
        assertThrows(MalformedJwtException.class,
            () -> {
                JwtUtils.getClaims("123456789", "token");
            });
    }

    /**
     * Test 4.
     *
     * @throws IOException the io exception
     * @since 1.0.0
     */
    @Test
    void test4() {
        Claims claims = JwtUtils.getClaims("1234567890", this.token);
        log.info("{}", JsonUtils.toJson(claims, true));
        ObjectMapper objectMapper = new ObjectMapper();
        AuthorizationUser user = objectMapper.convertValue(claims.get("user"), AuthorizationUser.class);
        log.info("{}", user);
        // 获取此访问用户所有角色拥有的权限资源
        List<AuthorizationRole> roles = objectMapper.convertValue(claims.get("roles"), new TypeReference<List<AuthorizationRole>>() {
        });
        log.info("{}", roles);
    }

    /**
     * Test convert.
     *
     * @since 1.0.0
     */
    @Test
    void testConvert() {
        ZekaClaims expectedResult = ZekaClaims.builder()
            .scope(Collections.singletonList("all"))
            .roles(Collections.singletonList(AuthorizationRole.builder().id(16L).roleName("超级管理员").build()))
            .exp(new Date(1573207413000L))
            .user(AuthorizationUser.builder().id(1L).mobile("18700000000").username("admin").build())
            .authorities(Collections.singletonList("超级管理员"))
            .jti("976c80a8-2239-480f-ba31-bdfa9a343cb9")
            .clientId("domi-ums")
            .build();

        // Run the test
        ZekaClaims result = JwtUtils.PlayGround.convert(this.key, this.token);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    /**
     * Test get authorities.
     *
     * @since 1.0.0
     */
    @Test
    void testGetAuthorities() {
        List<String> expectedResult = Collections.singletonList("超级管理员");

        // Run the test
        List<String> result = JwtUtils.PlayGround.getAuthorities(this.key, this.token);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    /**
     * Test get authorities 2
     *
     * @since 1.0.0
     */
    @Test
    void testGetAuthorities_2() {
        List<String> expectedResult = Collections.singletonList("超级管理员");

        // Run the test
        List<String> result = JwtUtils.PlayGround.getAuthorities(this.token);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    /**
     * token 解析失败会抛出异常
     *
     * @since 1.0.0
     */
    @Test
    void testGetClaims() {
        JwtUtils.getClaims(this.key, this.token);
    }

    /**
     * Test get client id.
     *
     * @since 1.0.0
     */
    @Test
    void testGetClientId() {
        String expectedResult = "domi-ums";

        // Run the test
        String result = JwtUtils.PlayGround.getClientId(this.key, this.token);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    /**
     * Test get client id 2
     *
     * @since 1.0.0
     */
    @Test
    void testGetClientId_2() {
        String expectedResult = "domi-ums";

        // Run the test
        String result = JwtUtils.PlayGround.getClientId(this.token);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    /**
     * Test get expiration.
     *
     * @since 1.0.0
     */
    @Test
    void testGetExpiration() {
        Date expectedResult = new Date(1573207413000L);
        this.token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9" +
            ".eyJ1c2VyX25hbWUiOiJhZG1pbiIsInNjb3BlIjpbImFsbCJdLCJyb2xlcyI6W3siaWQiOjE2LCJyb2xlTmFtZSI6Iui2hee6p" +
            "-euoeeQhuWRmCIsInJlbWFyayI6bnVsbH1dLCJleHAiOjE1NzMyOTE0MjYsInVzZXIiOnsiaWQiOjEsIm1vYmlsZSI6IjE4NzAwMDAwMDAwIiwidXNlcm5hbWUiOiJhZG1pbiIsImRlcHRDb2RlIjoiRktISlBUIn0sImF1dGhvcml0aWVzIjpbIui2hee6p-euoeeQhuWRmCJdLCJqdGkiOiI5NzZjODBhOC0yMjM5LTQ4MGYtYmEzMS1iZGZhOWEzNDNjYjkiLCJjbGllbnRfaWQiOiJma2hfZGFzaGJvYXJkIn0.xnDTGi285AWP3BGEQYPNo4xp-mxyFSBe5ujT8njh5iM";
        // Run the test
        Date result = JwtUtils.getExpiration(this.key, this.token);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    /**
     * Test get expiration 2
     *
     * @since 1.0.0
     */
    @Test
    void testGetExpiration_2() {
        Date expectedResult = new Date(1573207413000L);

        // Run the test
        Date result = JwtUtils.getExpiration(this.token);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    /**
     * Test get jti.
     *
     * @since 1.0.0
     */
    @Test
    void testGetJti() {
        String expectedResult = "976c80a8-2239-480f-ba31-bdfa9a343cb9";

        // Run the test
        String result = JwtUtils.getJti(this.key, this.token);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    /**
     * Test get jti 2
     *
     * @since 1.0.0
     */
    @Test
    void testGetJti_2() {
        String expectedResult = "976c80a8-2239-480f-ba31-bdfa9a343cb9";

        // Run the test
        String result = JwtUtils.getJti(this.token);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    /**
     * Test get jwt.
     *
     * @throws IOException io exception
     * @since 1.0.0
     */
    @Test
    void testGetJwt() throws IOException {
        // Setup
        String authentication = AuthConstant.BEARER + this.token;
        Jwt expectedResult = JwtUtils.getJwt(this.token);
        String claims = expectedResult.getClaims();
        String clientId = this.objectMapper.readTree(claims).get("client_id").asText();

        // Run the test
        Jwt result = JwtUtils.getJwt(authentication);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    /**
     * Test get jwt 1
     *
     * @since 1.0.0
     */
    @Test
    void testGetJwt_1() {
        assertThrows(IllegalArgumentException.class,
            () -> {
                String authentication = "token";
                Jwt expectedResult = JwtUtils.getJwt(authentication);
                assertNull(expectedResult);
            });

    }

    /**
     * Test get jwt 2
     *
     * @since 1.0.0
     */
    @Test
    void testGetJwt_2() {
        assertThrows(IllegalArgumentException.class,
            () -> {
                String authentication = AuthConstant.BEARER + "token";
                Jwt expectedResult = JwtUtils.getJwt(authentication);
                assertNull(expectedResult);
            });

    }

    /**
     * Test get roles.
     *
     * @since 1.0.0
     */
    @Test
    void testGetRoles() {
        List<AuthorizationRole> expectedResult = Collections.singletonList(AuthorizationRole.builder().id(16L).roleName("超级管理员").build());

        // Run the test
        List<AuthorizationRole> result = JwtUtils.PlayGround.getRoles(this.key, this.token);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    /**
     * Test get roles 2
     *
     * @since 1.0.0
     */
    @Test
    void testGetRoles_2() {
        List<AuthorizationRole> expectedResult = Collections.singletonList(AuthorizationRole.builder().id(16L).roleName("超级管理员").build());

        // Run the test
        List<AuthorizationRole> result = JwtUtils.PlayGround.getRoles(this.token);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    /**
     * Test get scope.
     *
     * @since 1.0.0
     */
    @Test
    void testGetScope() {
        List<String> expectedResult = Collections.singletonList("all");

        // Run the test
        List<String> result = JwtUtils.getScope(this.key, this.token);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    /**
     * Test get scope 2
     *
     * @since 1.0.0
     */
    @Test
    void testGetScope_2() {
        List<String> expectedResult = Collections.singletonList("all");

        // Run the test
        List<String> result = JwtUtils.getScope(this.token);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    /**
     * Test get token.
     *
     * @since 1.0.0
     */
    @Test
    void testGetToken() {
        // Setup
        String auth = AuthConstant.BEARER + this.token;
        String expectedResult = this.token;

        // Run the test
        String result = JwtUtils.getToken(auth);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    /**
     * Test get user.
     *
     * @since 1.0.0
     */
    @Test
    void testGetUser() {
        AuthorizationUser expectedResult = AuthorizationUser.builder().id(1L).mobile("18700000000").username("admin").build();

        // Run the test
        AuthorizationUser result = JwtUtils.PlayGround.getUser(this.key, this.token);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    /**
     * Test get user 2
     *
     * @since 1.0.0
     */
    @Test
    void testGetUser_2() {
        AuthorizationUser expectedResult = AuthorizationUser.builder().id(1L).mobile("18700000000").username("admin").build();

        // Run the test
        AuthorizationUser result = JwtUtils.PlayGround.getUser(this.token);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    /**
     * Test get username.
     *
     * @since 1.0.0
     */
    @Test
    void testGetUsername() {
        String expectedResult = "admin";

        // Run the test
        String result = JwtUtils.PlayGround.getUsername(this.key, this.token);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    /**
     * Test get username 2
     *
     * @since 1.0.0
     */
    @Test
    void testGetUsername_2() {
        String expectedResult = "admin";

        // Run the test
        String result = JwtUtils.PlayGround.getUsername(this.token);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    /**
     * Test generate token
     *
     * @since 1.0.0
     */
    @SneakyThrows
    @Test
    void testGenerateToken() {
        String secretKey = "1234567890";
        Map<String, Object> claims = Maps.newHashMapWithExpectedSize(7);
        claims.put(ZekaClaims.USER_NAME, "dong4j");

        claims.put(ZekaClaims.USER,
            JwtUtils.MAPPER.writeValueAsString(AuthorizationUser.builder().id(1L).userType(UserType.SHIPPER).mobile("18700000000").username(
                "admin").build()));
        List<AuthorizationRole> authorizationRoles =
            Collections.singletonList(AuthorizationRole.builder().id(16L).roleName("超级管理员").build());
        claims.put(ZekaClaims.ROLES, authorizationRoles);
        List<String> scopes = Collections.singletonList("all");
        claims.put(ZekaClaims.SCOPE, scopes);
        claims.put(ZekaClaims.AUTHORITIES, Collections.singletonList("超级管理员"));
        claims.put(ZekaClaims.JTI, StringUtils.getUid());
        claims.put(ZekaClaims.CLIENT_ID, "domi-ums");
        // 5 分钟过期
        String token = JwtUtils.generateToken(claims, secretKey, new Date(System.currentTimeMillis() + 5 * 60 * 1000L));
        log.info("{}", token);
        JwtUtils.PlayGround.getUsername(secretKey, token);
        JwtUtils.PlayGround.getUser(secretKey, token);
        JwtUtils.PlayGround.getUser(token);
    }

    /**
     * Test 1
     *
     * @since 1.0.0
     */
    @Test
    void test_1() {
        Date expiration = new Date(System.currentTimeMillis() + 60 * 1000L);
        JwtTokenInfo jwtTokenInfo = JwtTokenInfo.builder().userId(1L).username("dong4j").time(new Date()).build();
        jwtTokenInfo.setExp(expiration.getTime());
        Map<String, Object> stringObjectMap = BeanUtils.toMap(jwtTokenInfo);
        String token = JwtUtils.generateToken(stringObjectMap, "123456", expiration);
        log.info("{}", token);

        log.info("{}", JwtUtils.getExpiration(token));
    }

    /**
     * Test 2
     *
     * @since 1.0.0
     */
    @Test
    void test_2() {
        Date expiration = new Date(System.currentTimeMillis() + 30 * 60 * 1000L);

        Map<String, Object> claims = Maps.newHashMapWithExpectedSize(3);
        claims.put("username", "dong4j");
        claims.put("userId", "dong4j");
        claims.put("time", new Date());

        String token = JwtUtils.generateToken(claims, "123456", expiration);
        log.info("{}", token);
    }

    /**
     * Test is expiration
     *
     * @since 1.0.0
     */
    @Test
    void testIsExpiration() {
        log.info("{}", JwtUtils.isExpiration(this.token));
    }

    /**
     * Test
     *
     * @since 1.0.0
     */
    @Test
    void test() {

        log.info("{}", JsonUtils.toJson(new Date()));
    }

    /**
     * Test 11
     *
     * @since 1.0.0
     */
    @Test
    void test11() {
        String token = Jwts.builder()
            .setClaims(new HashMap<String, Object>(2) {
                private static final long serialVersionUID = 8694287116841334617L;

                {
                    this.put("", "");
                }
            })
            .signWith(SignatureAlgorithm.HS256, "1234567890")
            .compact();

        JwtUtils.isExpiration(token);
    }

    @Test
    void test_12() {
        log.info("{}", JwtUtils.getJwt("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9" +
            ".eyJ1c2VyX25hbWUiOiJkb25nNGoiLCJzY29wZSI6WyJhbGwiXSwicm9sZXMiOlt7ImlkIjoxLCJyb2xlTmFtZSI6bnVsbH1dLCJjbGllbnRfdHlwZSI6IntcInZhbHVlXCI6MyxcImRlc2NcIjpcIuWkmuenn-aIt-WtkOW6lOeUqC3kuJrliqHns7vnu59cIn0iLCJleHAiOjE1OTQwNjY3MTgsInVzZXIiOiJ7XCJpZFwiOjEsXCJ1c2VybmFtZVwiOlwiZG9uZzRqXCIsXCJtb2JpbGVcIjpcIjE4NjI4MzYyOTA2XCIsXCJlbWFpbFwiOlwiYXJyYXlkc2pAMTYzLmNvbVwiLFwidXNlclR5cGVcIjp7XCJ2YWx1ZVwiOjIsXCJkZXNjXCI6XCLlubPlj7DnlKjmiLct6am-6am25ZGYXCJ9LFwidGVuYW50SWRcIjowfSIsImFwcF9pZCI6MywiYXV0aG9yaXRpZXMiOltudWxsXSwianRpIjoiMjYwYWU0NmItYWE3OC00ZGViLWFiYzctMThiOTVjYzYwOTkyIiwiY2xpZW50X2lkIjoidGVzdF9jbGllbnQifQ.kMtTwE7Qm3EnIPvoxU_dRO4sXtmc920PCD2LZXVLm7o"));
    }
}
