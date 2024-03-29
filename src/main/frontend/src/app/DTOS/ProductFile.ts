export enum FileState {
  UPLOADING = "UPLOADING"
  , STORED = "STORED", SCANNING = "SCANNING", ERROR = "ERROR",
  ON_QUEUE = "ON_QUEUE",MALICIOUS = "MALICIOUS"
}

export class ProductFile{
		id:number;
		name:string;
		type:string;
  size: number;
  lastFile:boolean
  fileState: FileState;
  createdDate: Date;
  lastModified: Date;
  newUploaded: boolean;
  reviewed: boolean;
  enabled: boolean;
		constructor(){
			this.id=-1;
			this.name="";
      this.type = "";
      this.lastFile = false;
      this.size = -1;
      this.createdDate = new Date;
      this.lastModified = new Date;
      this.reviewed = false;
      this.enabled = false;
      this.newUploaded = false;
      this.fileState = FileState.STORED;
    }

    public static fromFile(file: File): ProductFile {
      const ret = new ProductFile;
      ret.id = -1;
      ret.name = file.name;
      ret.type = file.type;
      ret.size = file.size;
      ret.fileState = FileState.UPLOADING;
      return ret;
    }
	}
