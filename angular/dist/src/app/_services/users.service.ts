import {Injectable} from '@angular/core';
import {Http, Response, Headers, RequestOptions} from '@angular/http';
import {URL, user} from './url-infos'

let header = new Headers({'Content-Type': 'application/json'});

@Injectable()
export class UsersService {

    constructor(private http: Http) {
    }

    getUser() {
        return this.http.get(URL + "/mba/users/" + user, this.jwt(1)).map((response: Response) => response.json());
    }

    modifyUser(data) {
        this.http.put(URL + "/mba/users/" + user + "/", data, this.jwt(1));
    }

    private jwt(json = 0) {
        // create authorization header with jwt token
        let currentUser = JSON.parse(localStorage.getItem('currentUser'));
        if (currentUser && currentUser.AUTH_TOKEN) {
            let headers = new Headers;
            if (json == 1) {
                headers = new Headers({'Authorization': currentUser.AUTH_TOKEN, 'Content-Type': 'application/json'});
            } else {
                headers = new Headers({'Authorization': currentUser.AUTH_TOKEN});
            }
            return new RequestOptions({headers: headers});
        }
    }

}