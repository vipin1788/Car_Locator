package com.karsync.karsyncservices.config

import com.fasterxml.classmate.TypeResolver
import com.google.common.base.Predicates
import com.google.common.collect.Multimap
import com.karsync.karsyncservices.rest.requests.UserLoginRequest
import com.karsync.karsyncservices.rest.response.UserResponse
import com.karsync.karsyncservices.utils.KarSyncConstants
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.http.HttpMethod
import springfox.documentation.RequestHandler
import springfox.documentation.builders.ApiListingBuilder
import springfox.documentation.builders.OperationBuilder
import springfox.documentation.builders.ParameterBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.schema.ModelRef
import springfox.documentation.service.*
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spi.service.OperationBuilderPlugin
import springfox.documentation.spi.service.contexts.OperationContext
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.spring.web.plugins.DocumentationPluginsManager
import springfox.documentation.spring.web.readers.operation.CachingOperationNameGenerator
import springfox.documentation.spring.web.scanners.ApiDescriptionReader
import springfox.documentation.spring.web.scanners.ApiListingScanner
import springfox.documentation.spring.web.scanners.ApiListingScanningContext
import springfox.documentation.spring.web.scanners.ApiModelReader
import springfox.documentation.swagger2.annotations.EnableSwagger2

import java.util.ArrayList
import java.util.Arrays
import java.util.Collections
import java.util.HashSet
import java.util.LinkedList


@Configuration
@EnableSwagger2
class SwaggerConfig : OperationBuilderPlugin {

    @Autowired
    private val typeResolver: TypeResolver? = null

    @Bean
    fun productApi(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(Predicates.not<RequestHandler>(RequestHandlerSelectors.basePackage("org.springframework.boot")))
                .paths(PathSelectors.any())
                .build()
                .useDefaultResponseMessages(true)
                .additionalModels(typeResolver!!.resolve(UserLoginRequest::class.java))
                .additionalModels(typeResolver!!.resolve(UserResponse::class.java))
                .enableUrlTemplating(true)
    }

    override fun supports(documentationType: DocumentationType): Boolean {
        return DocumentationType.SWAGGER_2 == documentationType
    }

    override fun apply(context: OperationContext) {
        if (!context.requestMappingPattern().matches(KarSyncConstants.PUBLIC_SECURE_URL.toRegex())) {
            val parameters = ArrayList<Parameter>()
            val parameterBuilder = ParameterBuilder()
            parameterBuilder.name("Authorization")
                    .description("Authorization Token")
                    .modelRef(ModelRef("String"))
                    .parameterType("header")
                    .required(true)
                    .build()
            parameters.add(parameterBuilder.build())
            context.operationBuilder().parameters(parameters)
        }
    }

    @Primary
    @Bean
    fun addExtraOperations(apiDescriptionReader: ApiDescriptionReader, apiModelReader: ApiModelReader, pluginsManager: DocumentationPluginsManager): ApiListingScanner {
        return LoginApiScanner(apiDescriptionReader, apiModelReader, pluginsManager)
    }

    private inner class LoginApiScanner (apiDescriptionReader: ApiDescriptionReader, apiModelReader: ApiModelReader, pluginsManager: DocumentationPluginsManager) : ApiListingScanner(apiDescriptionReader, apiModelReader, pluginsManager) {

        override fun scan(context: ApiListingScanningContext): Multimap<String, ApiListing> {
            val def = super.scan(context)

            val responseMessages = HashSet<ResponseMessage>()
            responseMessages.add(ResponseMessage(200, "Login successful", ModelRef("UserResponse"), Collections.EMPTY_MAP as MutableMap<String, Header>?, Collections.EMPTY_LIST as MutableList<VendorExtension<Any>>?))

            val tags = HashSet<String>()
            tags.add("Authentication")

            val tagname = HashSet<Tag>()
            tagname.add(Tag("Authentication", "Authentication Controller"))

            val apis = LinkedList<ApiDescription>()

            val operations = ArrayList<Operation>()
            operations.add(OperationBuilder(CachingOperationNameGenerator())
                    .method(HttpMethod.POST)
                    .uniqueId("Login")
                    .parameters(Arrays.asList(ParameterBuilder()
                            .name("body")
                            .required(true)
                            .description("Login Request Body")
                            .parameterType("body")
                            .modelRef(ModelRef("UserLoginRequest"))
                            .build()))
                    .summary("Log in")
                    .responseModel(ModelRef("UserResponse"))
                    .responseMessages(responseMessages)
                    .tags(tags)
                    .build())
            apis.add(ApiDescription("/api/v1/public/login", "User Login", operations, false))

            def.put("authentication", ApiListingBuilder(context.documentationContext.apiDescriptionOrdering)
                    .apis(apis)
                    .description("Authorization API")
                    .tags(tagname)
                    .build())
            return def
        }

    }
}
