package com.aburakkontas.manga_main.api.controllers.workspaces;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/workspaces")
public class WorkspacesQueryController {

    // get workspaces ../workspaces/someid
    // get workspace works ../workspaces/someid/works
    // get workspace works work ../workspaces/someid/works/someid
    // get workspace history ../workspaces/someid/history
    // get workspace work history ../workspaces/someid/works/someid/history (history means logs)
    // get workspace users ../workspaces/someid/users
    // get workspace user ../workspaces/someid/users/someid (returns what user profile like what user can do etc.)
    // get workspace user history ../workspaces/someid/users/someid/history (returns what user did)
}
