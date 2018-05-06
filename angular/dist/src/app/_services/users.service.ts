import {Injectable} from '@angular/core';
import {Http, Response, Headers, RequestOptions} from '@angular/http';
import {URL, jwt, getCurrentUser} from './url-infos'

let header = new Headers({'Content-Type': 'application/json'});

@Injectable()
export class UsersService {

    constructor(private http: Http) {
    }

    getUser() {
        return this.http.get(URL + "/mba/users/" + getCurrentUser(), jwt(1)).map((response: Response) => response.json());
    }

    modifyUser(data) {
        this.http.put(URL + "/mba/users/" + getCurrentUser() + "/", data, jwt(1));
    }

}