import { Injectable } from '@angular/core';
import { Http, Response, Headers } from '@angular/http';
import { URL, user} from './url-infos'

let header = new Headers({'Content-Type': 'application/json'});
@Injectable()
export class UsersService {

 constructor(private http: Http) { }

 getUser() {
    return this.http.get(URL+"/mba/users/"+user).map((response: Response) => response.json());
 }

 modifyUser(data) {
    this.http.put(URL+"/mba/users/"+user+"/", data, {headers: header});
 }

}