package com.aburakkontas.manga_main.api.controllers.workspaces;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/workspaces")
public class WorkspacesCommandController {

    // create workspace ../workspaces/create
    // update workspace ../workspaces/update/someid
    // delete workspace ../workspaces/delete/someid
    // create workspace work ../workspaces/someid/create/works
    // update workspace work ../workspaces/someid/works/update/someid
    // delete workspace work ../workspaces/someid/works/delete/someid
    // invite workspace user ../workspaces/someid/invite
    // update workspace user ../workspaces/someid/users/update/someid
    // delete workspace user ../workspaces/someid/users/delete/someid

}
