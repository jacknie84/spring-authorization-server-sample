package com.jacknie.sample.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.PagedModel
import org.springframework.web.bind.annotation.*

@FeignClient(name = "post-api", path = "/posts")
interface PostClient {

    @GetMapping
    fun getPosts(): PagedModel<Post>

    @PostMapping
    fun createPost(post: CreatePost): EntityModel<Post>

    @GetMapping("/{postId}")
    fun getPost(@PathVariable("postId") postId: Long): EntityModel<Post>

    @PatchMapping("/{postId}")
    fun updatePost(@PathVariable("postId") postId: Long, post: UpdatePost): EntityModel<Post>

    @DeleteMapping("/{postId}")
    fun deletePost(@PathVariable("postId") postId: Long)
}