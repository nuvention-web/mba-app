// The URL of the website
import {Headers, RequestOptions} from "@angular/http";

// export const URL = "https://portal.myapp.mba";
 export const URL = "http://localhost:8080";

// The reset password must use this endpoint. Otherwise the server report 400. export const URL = "http://myappmba-199623.appspot.com";

export function getCurrentUser() {
    let currentUser = JSON.parse(localStorage.getItem('currentUser'));
    if(currentUser && currentUser.email) {
        return currentUser.email;
    } else {
        return false;
    }
}

export function jwt(json=0) {
    // create authorization header with jwt token
    let currentUser = JSON.parse(localStorage.getItem('currentUser'));
    if (currentUser && currentUser.AUTH_TOKEN) {
        let headers = new Headers;
        if(json==1) {
            headers = new Headers({'Authorization': currentUser.AUTH_TOKEN, 'Content-Type': 'application/json'});
        } else {
            headers = new Headers({ 'Authorization': currentUser.AUTH_TOKEN});
        }
        return new RequestOptions({ headers: headers });
    }
}