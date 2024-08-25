import {Injectable} from '@angular/core';

import {filesize} from "filesize"

@Injectable({
  providedIn: 'root'
})
export class FilesizeService {

  constructor() {
  }

  size(size: number): string {
    return filesize(size)
  }

}
