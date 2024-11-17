package com.jacknie.sample.client

import org.springframework.data.domain.Pageable
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@Controller
@RequestMapping("/posts")
class PostController(private val postClient: PostClient) {

    @GetMapping
    fun getPosts(pageable: Pageable, model: Model): String {
        val pagedModel = postClient.getPosts()
        model.addAttribute("pagedModel", pagedModel)
        return "posts/index"
    }

    @GetMapping("/form")
    fun form(): String {
        return "posts/form"
    }

    @PostMapping("/create")
    fun create(post: CreatePost, attributes: RedirectAttributes): String {
        val entityModel = postClient.createPost(post)
        return "redirect:/posts/${entityModel.content?.id}"
    }

    @GetMapping("/{postId}")
    fun view(@PathVariable("postId") postId: Long, model: Model): String {
        val entityModel = postClient.getPost(postId);
        model.addAttribute("post", entityModel.content)
        return "posts/view"
    }

    @GetMapping("/{postId}/form")
    fun form(@PathVariable("postId") postId: Long, model: Model): String {
        val entityModel = postClient.getPost(postId);
        model.addAttribute("post", entityModel.content)
        return "posts/form"
    }

    @PostMapping("/{postId}/update")
    fun update(@PathVariable("postId") postId: Long, post: UpdatePost, attributes: RedirectAttributes): String {
        val entityModel = postClient.updatePost(postId, post)
        return "redirect:/posts/${entityModel.content?.id}"
    }

    @PostMapping("/{postId}/delete")
    fun delete(@PathVariable("postId") postId: Long): String {
        postClient.deletePost(postId)
        return "redirect:/posts"
    }
}