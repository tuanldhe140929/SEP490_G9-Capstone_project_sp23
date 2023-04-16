package com.SEP490_G9.config.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;

import com.SEP490_G9.config.filter.JwtRequestFilter;
import com.SEP490_G9.security.JwtAuthenticationEntryPoint;

@Configuration
@Component
@EnableWebSecurity(debug = true)
public class SecurityConfig {
	@Autowired
	JwtAuthenticationEntryPoint authenEntryPoint;

	@Value("${spring.websecurity.debug:false}")
	boolean webSecurityDebug;

	@Autowired
	AuthenticationProvider authProvider;

	@Autowired
	JwtRequestFilter filter;

	private String[] publicApis= {
			"/account/login",
			"/account/logout",
			"/account/resetPassword",
			"/user/register",
			"/user/loginWithGoogle",
			"/user/sendVerifyEmail",
			"/user/verifyEmail/**",
			"/category/categories",
			"/category/addCategory",
			"/category/updateCategory/**",
			"/productDetails/getProductsForSearching",
			"/productDetails/getAllProducts",
			"/productDetails/getPublishedProductsBySeller",
			"/productDetails/getActiveVersion",
			"/productDetails/getByIdAndVersion",
			"/productDetails/GetAllProductForHomePage",
			"/productDetails/getTotalPurchasedCount",
			"/productDetails/allProductsLatestVers",
			"/refreshToken/refresh",
			"/seller/getSeller",
			"/seller/getSellersForSearching",
			"/seller/createNewSeller",
			"/seller/getSellerByPaypalEmail",
			"/public/serveMedia/image",
			"/public/serveMedia/video",
			"/violation/getAllVioTypes",
			"/product/getProductsCountBySellerId",
			"/user/getUserInfo",
			"private/manageTag/tags",
			"/user/getAllUsers"
			};
	
	private String[] userApis= {
			"/user/changeAccountPassword",
			"/user/uploadProfileImage",
			"/user/changeAccountInfo",
			"/private/cart/add/**",
			"/private/cart/remove/**",
			"/getCurrentCartDTO",
			"/removeAll/**",
			"/isUserPurchasedProduct",
			"/product/getLicense",
			"/product/getProductsCountBySellerId",
			"/productDetails/getActiveVersionForDownload",
			"/productFile/generateDownloadToken/**",
			"/productFile/download",
			"/report/sendReport",
			"/repott/getByProductUserVersion",
			"/transaction/purchase",
			"/transaction/reviewTransaction",
			"/transaction/cancel/**",
			"/transaction/executeTransaction",
			"/transaction/checkTransactionStatus",
			"/transaction/getPurchasedProductList",
			};
	
	private String[] sellerApis= {
			"/payout/getPayoutHistory",
			"/product/createNewProduct",
			"/product/deleteProduct/**",
			"/product/activeVersion",
			"/product/getProductDetails",
			"/productDetails/getProductsBySellerForSeller",
			"/productDetails/getAllVersion",
			"/productDetails/verifyProduct",
			"/productDetails/cancelVerifyProduct",
			"/productDetails/createNewVersionV2",
			"/productDetails/updateProduct",
			"/productFile/uploadProductFile",
			"/productFile/deleteProductFile",
			
			};
	
	private String[] adminApis= {
			"/account/staffs",
			"/account/addStaff",
			"/account/updateStaffStatus",
			"/account/allAccounts"
			
			};
	
	private String[] staffApis= {
			"/product/updateProductApprovalStatus",
			"/product/getProductsByReportStatus",
			"/productDetails/getProductsByReportStatus",
			"/productDetails/getByApprovalStatus",
			"/productDetails/updateApprovalStatus",
			"/report/updateReportStatus",
			"/report/getByProductAndStatus",
			"/seller/reportedsellerlist",
			"/violation/addViolation"
			};
	@Bean
	public SecurityFilterChain setfilterChains(HttpSecurity http) throws Exception {

		http.cors().configurationSource(request -> {
			CorsConfiguration configuration = new CorsConfiguration();
			configuration.setAllowedOrigins(List.of("http://localhost:4200","ap.ngrok.io"));
			configuration.setAllowCredentials(true);
			configuration.setAllowedMethods(List.of("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
			configuration.addExposedHeader("Message");
			configuration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type"));
			return configuration;
		}).and().csrf().disable();
		
		

		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);

		http.authorizeHttpRequests().requestMatchers(publicApis).permitAll()
		.and().authorizeHttpRequests().requestMatchers(userApis).hasAnyRole("USER","STAFF")
		.and().authorizeHttpRequests().requestMatchers(sellerApis).hasAnyRole("SELLER")
		.and().authorizeHttpRequests().requestMatchers(adminApis).hasAnyRole("ADMIN")
		.and().authorizeHttpRequests().requestMatchers(staffApis).hasAnyRole("STAFF","ADMIN")
		.and().httpBasic().authenticationEntryPoint(authenEntryPoint)
		.and().authorizeHttpRequests().anyRequest().authenticated()
		.and().authenticationProvider(authProvider)
		.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

//	@Bean
//	public WebMvcConfigurer corsConfigurer() {
//		return new WebMvcConfigurer() {
//			@Override
//			public void addCorsMappings(CorsRegistry registry) {
//				registry.addMapping("/**").allowedMethods("*").allowedOrigins("http://localhost:4200")
//				.allowCredentials(true).exposedHeaders("Message").allowedHeaders("*");
//			}
//		};
//	}

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.debug(webSecurityDebug);
	}

	@Bean
	public SessionRegistry sessionRegistry() {
		return new SessionRegistryImpl();
	}

	@Bean
	public HttpSessionEventPublisher httpSessionEventPublisher() {
		return new HttpSessionEventPublisher();
	}

}
