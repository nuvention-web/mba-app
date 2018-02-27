import { Component, OnInit, ViewEncapsulation, AfterViewInit } from '@angular/core';
import { ScriptLoaderService } from '../../../../_services/script-loader.service';
import { UsersSerivce } from '../../../../_services/users.service';

@Component({
    selector: "app-profile",
    templateUrl: "./profile.component.html",
    encapsulation: ViewEncapsulation.None,
})
export class ProfileComponent implements OnInit, AfterViewInit {

    userInformation: any = {};

    constructor(private _script: ScriptLoaderService, private _user: UsersSerivce) {
        this._user.getUser().subscribe(d => {
            var tmp = d.name.split(" ");
            this.userInformation = d;
            this.userInformation.firstName = tmp[0];
            this.userInformation.lastName = tmp[1]; 
        });
    }
    ngOnInit() {

    }
    ngAfterViewInit() {
        this._script.loadScripts('app-validation-form-controls',
            ['assets/demo/default/custom/components/forms/validation/form-controls.js']);
    }

    userInformationUpdate() {
        this.userInformation.name = this.userInformation.firstName + " " + this.userInformation.lastName;
        delete this.userInformation.firstName;
        delete this.userInformation.lastName;
        delete this.userInformation.schools;
        this._user.modifyUser(JSON.stringify(this.userInformation));
    }

}