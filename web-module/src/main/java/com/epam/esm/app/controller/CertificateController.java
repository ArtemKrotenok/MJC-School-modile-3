package com.epam.esm.app.controller;

import com.epam.esm.app.model.Page;
import com.epam.esm.app.model.PageModel;
import com.epam.esm.app.util.PageUtil;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.model.CertificateDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * controller class for <b>gift certificate</b>.
 *
 * @author Artem Krotenok
 * @version 1.0
 */

@RestController
@RequestMapping(value = "/certificates", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor

public class CertificateController {

    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final int DEFAULT_FIRST_PAGE = 1;
    public CertificateService certificateService;

    /**
     * controller for create new gift certificate
     *
     * @param certificateDTO - object contain new gift certificate model
     * @return CertificateDTO
     */
    @PostMapping
    public ResponseEntity<Object> createCertificate(@RequestBody CertificateDTO certificateDTO) {
        CertificateDTO newCertificateDTO = certificateService.create(certificateDTO);
        newCertificateDTO.add(linkTo(methodOn(CertificateController.class).getCertificateById(newCertificateDTO.getId())).withSelfRel());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(newCertificateDTO);
    }

    /**
     * controller for update gift certificate
     *
     * @param certificateDTO - object contain update data for gift certificate model
     */
    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateCertificate(@RequestBody CertificateDTO certificateDTO) {
        certificateService.update(certificateDTO);
    }

    /**
     * controller for getting a list of gift certificates by page (using pagination)
     * search by tag, name, description
     * sorted by name gift certificates
     *
     * @param page        - number page for return
     * @param tag         - one of tag for gift certificate (not required)
     * @param name        - name for gift certificate (not required)
     * @param description - description for gift certificate (not required)
     * @return list CertificateDTO
     */
    @GetMapping
    public ResponseEntity<Object> searchCertificates(
            @RequestParam(name = "page") Integer page,
            @RequestParam(name = "size") Integer size,
            @RequestParam(name = "tag") String tag,
            @RequestParam(name = "name") String name,
            @RequestParam(name = "description") String description
    ) {
        List<CertificateDTO> certificateDTOs = certificateService.search(page, size, tag, name, description);
        certificateDTOs.forEach(this::addLinksInfo);
        PageModel<List<CertificateDTO>> response = new PageModel<>(certificateDTOs);
        long countCertificates = certificateService.getCountSearch(tag, name, description);
        Page pageInfo = PageUtil.getPageInfo(page, size, countCertificates);
        response.setPage(pageInfo);
        if (page >= 2) {
            response.getLinks().add(linkTo(methodOn(CertificateController.class).searchCertificates(page - 1, size, tag, name, description)).withRel("previousPage"));
        }
        if (page < pageInfo.getTotalPages()) {
            response.getLinks().add(linkTo(methodOn(CertificateController.class).searchCertificates(page + 1, size, tag, name, description)).withRel("nextPage"));
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    /**
     * controller for getting gift certificate by id
     *
     * @param id - id number gift certificate in database
     * @return CertificateDTO
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<Object> getCertificateById(@PathVariable(name = "id") Long id) {
        CertificateDTO certificateDTO = certificateService.findById(id);
        addLinksInfo(certificateDTO);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(certificateDTO);
    }

    /**
     * controller for delete gift certificate
     *
     * @param id - id number gift certificate in database
     */
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCertificateById(@PathVariable(name = "id") long id) {
        certificateService.deleteById(id);
    }

    private void addLinksInfo(CertificateDTO certificateDTO) {
        certificateDTO.add(linkTo(methodOn(CertificateController.class).getCertificateById(certificateDTO.getId())).withSelfRel());
        certificateDTO.getTags().forEach(tagDTO ->
                tagDTO.add(linkTo(methodOn(CertificateController.class).searchCertificates(DEFAULT_FIRST_PAGE, DEFAULT_PAGE_SIZE, tagDTO.getName(), "", "")).withRel("searchByTagName")));
    }
}