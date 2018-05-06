import { Component, OnInit, ViewEncapsulation, AfterViewInit } from '@angular/core';
import { Helpers } from '../../../helpers';
import { UsersService } from '../../../_services/users.service';


declare let mLayout: any;
@Component({
    selector: "app-header-nav",
    templateUrl: "./header-nav.component.html",
    encapsulation: ViewEncapsulation.None,
    providers: [UsersService]
})
export class HeaderNavComponent implements OnInit, AfterViewInit {
    user = {};

    constructor(private _users:UsersService) {
        this._users.getUser().subscribe(d => this.user = d);
    }
    ngOnInit() {

    }
    ngAfterViewInit() {

        mLayout.initHeader();

    }

}