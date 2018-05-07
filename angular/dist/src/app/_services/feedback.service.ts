import {Injectable} from '@angular/core';
import {Http, Response, Headers} from '@angular/http';
import 'rxjs/add/operator/map';
import { URL} from './url-infos'

const header =  new Headers( {'Content-Type': 'application/json'});

// URL example: may be something likes this https://{url}/feedback/{username}/{schoolname}/{essayname}/{token}/{reviewID}

@Injectable()
export class FeedbackService {
    info = {};
    constructor(private http: Http) {

    }

    parseURL(url): any{
        console.log(url);
        let arr = url.split('/');
        return {'url': URL, 'user': arr[2], 'school': arr[3], 'essay': arr[4], 'token': arr[5], 'reviewid': arr[6], 'view': arr[7]};
    }

    getData(url) {
        this.info = this.parseURL(url);
        return this.http.get( this.info['url'] + '/review/users/' + this.info['user']
            + '/school/' + this.info['school'] + '/essay/' + this.info['essay'] +  '/draft/' + this.info['token'] + '/review/' + this.info['reviewid']).map((response: Response) => response.json());
    }

    uploadComment(content) {
        return this.http.post( this.info['url'] + '/review/users/' + this.info['user']
            + '/school/' + this.info['school'] + '/essay/' + this.info['essay'] +  '/draft/' + this.info['token'] + '/review/' + this.info['reviewid'], content, {headers: header});
    }

    uploadFile(file) {
        if (file == null || file['file'] == null || file['name'] == null) {
            return;
        }
        const formData: FormData = new FormData();
        formData.append('file', file['file'], file['name']);
        return this.http.post( this.info['url'] + '/review/users/' + this.info['user']
            + '/school/' + this.info['school'] + '/essay/' + this.info['essay'] +  '/draft/' + this.info['token'] + '/review/' + this.info['reviewid'] + '/upload', formData);
    }
}

