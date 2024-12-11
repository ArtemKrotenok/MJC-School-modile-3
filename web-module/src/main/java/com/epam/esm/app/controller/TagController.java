package com.epam.esm.app.controller;

import com.epam.esm.app.model.Page;
import com.epam.esm.app.model.PageModel;
import com.epam.esm.app.util.LinkUtil;
import com.epam.esm.app.util.PageUtil;
import com.epam.esm.service.TagService;
import com.epam.esm.service.model.TagDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * controller class for <b>tag</b> (for gift certificate).
 *
 * @author Artem Krotenok
 * @version 1.0
 */

@RestController
@RequestMapping(value = "/tags", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor

public class TagController {

    public TagService tagService;

    /**
     * controller for create new tag
     *
     * @param tagDTO - object contain new tag model
     * @return created TagDTO
     */
    @PostMapping
    public ResponseEntity<Object> createTag(@RequestBody TagDTO tagDTO) {
        TagDTO newTagDTO = tagService.create(tagDTO);
        LinkUtil.addLinksInfo(newTagDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(newTagDTO);
    }

    /**
     * controller for getting a list of tag by page using pagination (list sorted by name tag)
     *
     * @param page - number page for return
     * @param size - number of items per page
     * @return list TagDTO
     */
    @GetMapping
    public ResponseEntity<Object> getTagsByPageSorted(
            @RequestParam(name = "page") int page,
            @RequestParam(name = "size") int size) {
        List<TagDTO> tagDTOs = tagService.getAllByPageSorted(page, size);
        tagDTOs.forEach(LinkUtil::addLinksInfo);
        PageModel<List<TagDTO>> response = new PageModel<>(tagDTOs);
        long countTags = tagService.getCount();
        Page pageInfo = PageUtil.getPageInfo(page, size, countTags);
        response.setPage(pageInfo);
        if (page >= 2) {
            response.getLinks().add(linkTo(methodOn(TagController.class).getTagsByPageSorted(page - 1, size)).withRel("previousPage"));
        }
        if (page < pageInfo.getTotalPages()) {
            response.getLinks().add(linkTo(methodOn(TagController.class).getTagsByPageSorted(page + 1, size)).withRel("nextPage"));
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    /**
     * controller for getting tag by id
     *
     * @param id - id number tag in database
     * @return TagDTO
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<Object> getTagById(@PathVariable(name = "id") Long id) {
        TagDTO tagDTO = tagService.findById(id);
        LinkUtil.addLinksInfo(tagDTO);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(tagDTO);
    }

    /**
     * controller for getting super tag
     * *
     *
     * @return TagDTO
     */
    @GetMapping(value = "/super")
    public ResponseEntity<Object> getSuperTag() {
        TagDTO tagDTO = tagService.findSuper();
        LinkUtil.addLinksInfo(tagDTO);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(tagDTO);
    }

    /**
     * controller for delete tag
     *
     * @param id - id number gift tag in database
     */
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> deleteTagById(@PathVariable(name = "id") Long id) {
        tagService.deleteById(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(null);
    }
}