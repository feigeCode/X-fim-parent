
export interface IXiaoFeiIm {
    host: string;
    port: number;
    socket: WebSocket;
    connect(): void;
}

export class XiaoFeiIM implements IXiaoFeiIm{


    private normalClose: boolean = true;
    host: string;
    port: number;
    socket: WebSocket;

    constructor(host?: string, port?: number) {
        if (host && port){
            this.host = host;
            this.port = port;
        }else {
            this.host = window.location.host;
            this.port = 8090;
        }
        this.socket = new WebSocket(`ws://${this.host}:${this.port}/ws`);
        this.socket.binaryType = 'arraybuffer';
    }




    connect(): void{


    }

}

