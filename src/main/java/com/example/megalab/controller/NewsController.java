package com.example.megalab.controller;

import com.example.megalab.DTO.NewsDTO;
import com.example.megalab.entity.News;
import com.example.megalab.service.NewsService;
import com.example.megalab.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

@RestController
@RequestMapping("/sp")
public class NewsController {

    private NewsService newsService;
    private UserService userService;
    Logger logger = LoggerFactory.getLogger(NewsController.class);

    public NewsController(NewsService newsService,
                          UserService userService) {
        this.newsService = newsService;
        this.userService = userService;
    }
    @Operation(summary = "Get all news")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "comment add success",
                    content = { @Content}),
            @ApiResponse(responseCode = "403", description = "Client doesn't have the token",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "JWT token is expired or invalidate",
                    content = @Content)
    })
    @GetMapping("/news")
    public ResponseEntity<?> getSuccessPage(){
        List<News> newsList = newsService.findAll();;
        List<NewsDTO> newsDTOList = newsService.listToListDTO(newsList);
        logger.info("return news");
        return new ResponseEntity<>(newsDTOList, HttpStatus.OK);
    }

    @Operation(summary = "Add news")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "comment add success",
                    content = { @Content}),
            @ApiResponse(responseCode = "400", description = "News doesn't download",
            content = @Content),
            @ApiResponse(responseCode = "403", description = "Client doesn't have the token",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "JWT token is expired or invalidate",
                    content = @Content)
    })
    @PostMapping("/addNews")
    public ResponseEntity<?> addNews(@Parameter(description = "User's token") @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                     @Parameter(description = "News's image") @RequestParam("image") MultipartFile file,
                                     @Parameter(description = "String header") @RequestParam("header") String header,
                                     @Parameter(description = "String description") @RequestParam("description") String description,
                                     @Parameter(description = "String content") @RequestParam("content") String content,
                                     @Parameter(description = "You can use only this rubric: Спорт, Политика, Звезды, Искусство, Мода")@RequestParam("rubric") String rubric){
        logger.info("add news");
        return newsService.saveNews(token, header, description, content, rubric, file);
    }

    @Operation(summary = "Get news by Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "comment add success",
                    content = { @Content}),
            @ApiResponse(responseCode = "400", description = "News doesn't exist",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Client doesn't have the token",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "JWT token is expired or invalidate",
                    content = @Content)
    })
    @GetMapping("/getNews/{newsId}")
    public ResponseEntity<?> getNews(@Parameter(description = "News Id what you want to get")@PathVariable long newsId){
        News news;
        try{
            news = newsService.getNews(newsId);
        }catch (NoSuchElementException e){
            return new ResponseEntity<>("News doesn't exist",HttpStatus.BAD_REQUEST);
        }
        NewsDTO newsDTO = newsService.newsToNewsDTO(news);
        logger.info("return news by newsId:" + newsId);
        return new ResponseEntity<>(newsDTO,HttpStatus.OK);
    }

    @Operation(summary = "Get user's news")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "comment add success",
                    content = { @Content(mediaType = "application/json",
                        schema = @Schema(implementation = NewsDTO.class)) }),
            @ApiResponse(responseCode = "403", description = "Client doesn't have the token",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "JWT token is expired or invalidate",
                    content = @Content)
    })
    @GetMapping("/getUserNews")
    public ResponseEntity<?> getUserNews( @Parameter(description = "User's token") @RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        List<News> newsList = newsService.getUserNews(token);
        List<NewsDTO> newsDTOList = newsService.listToListDTO(newsList);
        logger.info("return user's news");
        return new ResponseEntity<>(newsDTOList, HttpStatus.OK);
    }

    @Operation(summary = "Delete news by Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "comment add success",
                    content = { @Content}),
            @ApiResponse(responseCode = "400", description = "News doesn't delete",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Client doesn't have the token",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "JWT token is expired or invalidate",
                    content = @Content)
    })
    @DeleteMapping("/deleteNews/{newsId}")
    @Transactional
    public ResponseEntity<?> deleteNews(@Parameter(description = "User's token") @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                        @Parameter(description = "News id what you want to delete") @PathVariable long newsId){
        logger.info("delete news's by newsId");
        return newsService.deleteNews(token, newsId);
    }

    @Operation(summary = "add like to news")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "comment add success",
                    content = { @Content}),
            @ApiResponse(responseCode = "403", description = "Client doesn't have the token",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "JWT token is expired or invalidate",
                    content = @Content)
    })
    @GetMapping("/like/{newsId}")
    public ResponseEntity<?> addLike(@Parameter(description = "User's token") @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                     @Parameter(description = "News id what you give like")@PathVariable long newsId){
        logger.info("add like to news:" + newsId);
        return userService.addLike(token, newsId);
    }

    @Operation(summary = "Fine news by header")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "comment add success",
                    content = { @Content}),
            @ApiResponse(responseCode = "403", description = "Client doesn't have the token",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "JWT token is expired or invalidate",
                    content = @Content)
    })
    @GetMapping("/findNews/{header}")
    public ResponseEntity<?> findNews(@Parameter(description = "Word what filter will be find news")@PathVariable String header){
        List<News> newsList = newsService.findNews(header);
        List<NewsDTO> newsDTOList = newsService.listToListDTO(newsList);
        logger.info("find news by header");
        return new ResponseEntity<>(newsDTOList, HttpStatus.OK);
    }

    @Operation(summary = "Fine all news by user liked")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "comment add success",
                    content = { @Content}),
            @ApiResponse(responseCode = "403", description = "Client doesn't have the token",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "JWT token is expired or invalidate",
                    content = @Content)
    })
    @GetMapping("/likedUser")
    @Transactional
    public ResponseEntity<?> likedUser(@Parameter(description = "User's token") @RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        Set<News> newsSet = userService.likedUser(token);
        List<News> newsList = new ArrayList<>(newsSet);
        List<NewsDTO> newsDTOList = newsService.listToListDTO(newsList);
        logger.info("return news what user create");
        return new ResponseEntity<>(newsDTOList, HttpStatus.OK);
    }


    @Operation(summary = "Filter by rubric")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "comment add success",
                    content = { @Content}),
            @ApiResponse(responseCode = "403", description = "Client doesn't have the token",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "JWT token is expired or invalidate",
                    content = @Content)
    })
    @PostMapping("/filterNews")
    public ResponseEntity<?> filterNews(@Parameter(description = "You can type: Спорт") @RequestParam("sport") String sport,
                                        @Parameter(description = "You can type: Политика") @RequestParam("politics") String politics,
                                        @Parameter(description = "You can type: Звезды") @RequestParam("stars") String stars,
                                        @Parameter(description = "You can type: Искусство") @RequestParam("art") String art,
                                        @Parameter(description = "You can type: Мода") @RequestParam("fashion") String fashion){
        logger.info("filter news");
        return newsService.filterNews(sport, politics, stars, art, fashion);
    }
}
