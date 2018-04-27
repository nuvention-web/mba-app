import {Injectable} from '@angular/core';
import {Http, Response, Headers} from '@angular/http';
import 'rxjs/add/operator/map';

const header =  new Headers( {'Content-Type': 'application/json'});
const globalURL = 'myappmba-199623.appspot.com';
const user = 'john.doe@gmail.com';

// URL example: may be something likes this https://{url}/feedback/{username}/{schoolname}/{essayname}/{token}/{reviewID}

@Injectable()
export class FeedbackService {
    info = {};
    constructor(private http: Http) {

    }

    parseURL(url): any{
        console.log(url);
        let arr = url.split('/');
        return {'url': globalURL, 'user': arr[2], 'school': arr[3], 'essay': arr[4], 'token': arr[5], 'reviewid': arr[6]};
    }

    getData(url) {
        this.info = this.parseURL(url);
        return this.http.get('https://' + this.info['url'] + '/feedback/users/' + this.info['user']
            + '/school/' + this.info['school'] + '/essay/' + this.info['essay'] +  '/draft/' + this.info['token'] + '/review/' + this.info['reviewid']).map((response: Response) => response.json());
    }

    uploadComment(content) {
        return this.http.post('https://' + this.info['url'] + '/feedback/users/' + this.info['user']
            + '/school/' + this.info['school'] + '/essay/' + this.info['essay'] +  '/draft/' + this.info['token'] + '/review/' + this.info['reviewid'], content, {headers: header});
    }

    uploadFile(file) {
        if (file == null || file['file'] == null || file['name'] == null) {
            return;
        }
        const formData: FormData = new FormData();
        formData.append('file', file['file'], file['name']);
        return this.http.post('https://' + this.info['url'] + '/feedback/users/' + this.info['user']
            + '/school/' + this.info['school'] + '/essay/' + this.info['essay'] +  '/draft/' + this.info['token'] + '/review/' + this.info['reviewid'] + '/upload', formData);
    }
}

