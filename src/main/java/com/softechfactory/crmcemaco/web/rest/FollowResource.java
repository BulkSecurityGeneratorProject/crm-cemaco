package com.softechfactory.crmcemaco.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.softechfactory.crmcemaco.service.FollowService;
import com.softechfactory.crmcemaco.web.rest.errors.BadRequestAlertException;
import com.softechfactory.crmcemaco.web.rest.util.HeaderUtil;
import com.softechfactory.crmcemaco.service.dto.FollowDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Follow.
 */
@RestController
@RequestMapping("/api")
public class FollowResource {

    private final Logger log = LoggerFactory.getLogger(FollowResource.class);

    private static final String ENTITY_NAME = "follow";

    private final FollowService followService;

    public FollowResource(FollowService followService) {
        this.followService = followService;
    }

    /**
     * POST  /follows : Create a new follow.
     *
     * @param followDTO the followDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new followDTO, or with status 400 (Bad Request) if the follow has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/follows")
    @Timed
    public ResponseEntity<FollowDTO> createFollow(@RequestBody FollowDTO followDTO) throws URISyntaxException {
        log.debug("REST request to save Follow : {}", followDTO);
        if (followDTO.getId() != null) {
            throw new BadRequestAlertException("A new follow cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FollowDTO result = followService.save(followDTO);
        return ResponseEntity.created(new URI("/api/follows/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /follows : Updates an existing follow.
     *
     * @param followDTO the followDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated followDTO,
     * or with status 400 (Bad Request) if the followDTO is not valid,
     * or with status 500 (Internal Server Error) if the followDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/follows")
    @Timed
    public ResponseEntity<FollowDTO> updateFollow(@RequestBody FollowDTO followDTO) throws URISyntaxException {
        log.debug("REST request to update Follow : {}", followDTO);
        if (followDTO.getId() == null) {
            return createFollow(followDTO);
        }
        FollowDTO result = followService.save(followDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, followDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /follows : get all the follows.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of follows in body
     */
    @GetMapping("/follows")
    @Timed
    public List<FollowDTO> getAllFollows() {
        log.debug("REST request to get all Follows");
        return followService.findAll();
        }

    /**
     * GET  /follows/:id : get the "id" follow.
     *
     * @param id the id of the followDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the followDTO, or with status 404 (Not Found)
     */
    @GetMapping("/follows/{id}")
    @Timed
    public ResponseEntity<FollowDTO> getFollow(@PathVariable Long id) {
        log.debug("REST request to get Follow : {}", id);
        FollowDTO followDTO = followService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(followDTO));
    }
    /**
     * DELETE  /follows/:id : delete the "id" follow.
     *
     * @param id the id of the followDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/follows/{id}")
    @Timed
    public ResponseEntity<Void> deleteFollow(@PathVariable Long id) {
        log.debug("REST request to delete Follow : {}", id);
        followService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/follows/next-contact/{date}")
    @Timed
    public List<FollowDTO> getFollow(@PathVariable String date) {
        List<FollowDTO> followDTOS = followService.findFollowByNextContact(date);
        return followDTOS;
    }

    @GetMapping("/follows/regsitry-date/{date}")
    @Timed
    public List<FollowDTO> findFollowByRegistryDate(@PathVariable String date) {
        List<FollowDTO> followDTOS = followService.findFollowByRegistryDate(date);
        return followDTOS;
    }
}
