import { Injectable } from "@angular/core";
import { Headers, Http, RequestOptions, Response } from "@angular/http";

import { User } from "../_models/index";
import { URL } from "../../_services/url-infos";
import {Header} from "primeng/primeng";


let header = new Headers({'Content-Type': 'application/json'});
@Injectable()
export class UserService {
    constructor(private http: Http) {
    }

    forgotPassword(email: string) {
        return this.http.post(URL + '/account/forgotPassword', {email: email}, {headers: header});
    }

    resetPassword(email:string, code: string, newPass: string) {
        return this.http.post(URL + '/account/resetPassword', {email: email, resetCode: code, newPassword: newPass}, {headers: header});
    }

    getById(id: number) {
        return this.http.get('/api/users/' + id, this.jwt(0)).map((response: Response) => response.json());
    }

    create(user: User) {
        return this.http.post(URL + '/account/create', {name: user.name, email: user.email, password: user.password},{headers: header});
    }

    update(user: User) {
        return this.http.put('/api/users/' + user.id, user, this.jwt(0)).map((response: Response) => response.json());
    }

    verify(email: string, code: string) {
        return this.http.post(URL + '/account/activate', {email: email, code: code}, {headers: header});
    }

    // private helper methods

    private jwt(json = 0) {
        // create authorization header with jwt token
        let currentUser = JSON.parse(localStorage.getItem('currentUser'));
        if (currentUser && currentUser.AUTH_TOKEN) {
            let headers = new Headers;
            if(json == 1) {
                headers = new Headers({'Authorization': currentUser.AUTH_TOKEN, 'Content-Type': 'application/json'});
            } else {
                headers = new Headers({ 'Authorization': currentUser.AUTH_TOKEN});
            }
            return new RequestOptions({ headers: headers });
        }
    }
}