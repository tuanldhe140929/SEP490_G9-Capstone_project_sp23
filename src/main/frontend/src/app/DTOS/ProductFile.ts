export enum FileState {
  UPLOADING,UPLOADED,SCANNING,ERROR
}

export class ProductFile{
		id:number;
		name:string;
		type:string;
		size:number;
  fileState: FileState;
		constructor(){
			this.id=-1;
			this.name="";
			this.type="";
      this.size = -1;
      this.fileState = FileState.UPLOADED;
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
