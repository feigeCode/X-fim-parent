/*eslint-disable block-scoped-var, id-length, no-control-regex, no-magic-numbers, no-prototype-builtins, no-redeclare, no-shadow, no-var, sort-vars*/
"use strict";

var $protobuf = require("protobufjs/minimal");

// Common aliases
var $Reader = $protobuf.Reader, $Writer = $protobuf.Writer, $util = $protobuf.util;

// Exported root namespace
var $root = $protobuf.roots["default"] || ($protobuf.roots["default"] = {});

$root.com = (function() {

    /**
     * Namespace com.
     * @exports com
     * @namespace
     */
    var com = {};

    com.feige = (function() {

        /**
         * Namespace feige.
         * @memberof com
         * @namespace
         */
        var feige = {};

        feige.im = (function() {

            /**
             * Namespace im.
             * @memberof com.feige
             * @namespace
             */
            var im = {};

            im.pojo = (function() {

                /**
                 * Namespace pojo.
                 * @memberof com.feige.im
                 * @namespace
                 */
                var pojo = {};

                pojo.proto = (function() {

                    /**
                     * Namespace proto.
                     * @memberof com.feige.im.pojo
                     * @namespace
                     */
                    var proto = {};

                    proto.Auth = (function() {

                        /**
                         * Properties of an Auth.
                         * @memberof com.feige.im.pojo.proto
                         * @interface IAuth
                         * @property {string|null} [ip] 登录IP
                         * @property {string|null} [deviceId] 设备ID
                         * @property {string|null} [deviceName] 设备名字
                         * @property {string|null} [version] APP版本
                         * @property {string|null} [osVersion] 系统版本
                         * @property {string|null} [language] 语言
                         * @property {string|null} [token] 登录token，没有token时可用作userId
                         * @property {string|null} [platform] 平台名
                         * @property {string|null} [address] 地址
                         */

                        /**
                         * Constructs a new Auth.
                         * @memberof com.feige.im.pojo.proto
                         * @classdesc Represents an Auth.
                         * @implements IAuth
                         * @constructor
                         * @param {com.feige.im.pojo.proto.IAuth=} [properties] Properties to set
                         */
                        function Auth(properties) {
                            if (properties)
                                for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                                    if (properties[keys[i]] != null)
                                        this[keys[i]] = properties[keys[i]];
                        }

                        /**
                         * 登录IP
                         * @member {string} ip
                         * @memberof com.feige.im.pojo.proto.Auth
                         * @instance
                         */
                        Auth.prototype.ip = "";

                        /**
                         * 设备ID
                         * @member {string} deviceId
                         * @memberof com.feige.im.pojo.proto.Auth
                         * @instance
                         */
                        Auth.prototype.deviceId = "";

                        /**
                         * 设备名字
                         * @member {string} deviceName
                         * @memberof com.feige.im.pojo.proto.Auth
                         * @instance
                         */
                        Auth.prototype.deviceName = "";

                        /**
                         * APP版本
                         * @member {string} version
                         * @memberof com.feige.im.pojo.proto.Auth
                         * @instance
                         */
                        Auth.prototype.version = "";

                        /**
                         * 系统版本
                         * @member {string} osVersion
                         * @memberof com.feige.im.pojo.proto.Auth
                         * @instance
                         */
                        Auth.prototype.osVersion = "";

                        /**
                         * 语言
                         * @member {string} language
                         * @memberof com.feige.im.pojo.proto.Auth
                         * @instance
                         */
                        Auth.prototype.language = "";

                        /**
                         * 登录token，没有token时可用作userId
                         * @member {string} token
                         * @memberof com.feige.im.pojo.proto.Auth
                         * @instance
                         */
                        Auth.prototype.token = "";

                        /**
                         * 平台名
                         * @member {string} platform
                         * @memberof com.feige.im.pojo.proto.Auth
                         * @instance
                         */
                        Auth.prototype.platform = "";

                        /**
                         * 地址
                         * @member {string} address
                         * @memberof com.feige.im.pojo.proto.Auth
                         * @instance
                         */
                        Auth.prototype.address = "";

                        /**
                         * Creates a new Auth instance using the specified properties.
                         * @function create
                         * @memberof com.feige.im.pojo.proto.Auth
                         * @static
                         * @param {com.feige.im.pojo.proto.IAuth=} [properties] Properties to set
                         * @returns {com.feige.im.pojo.proto.Auth} Auth instance
                         */
                        Auth.create = function create(properties) {
                            return new Auth(properties);
                        };

                        /**
                         * Encodes the specified Auth message. Does not implicitly {@link com.feige.im.pojo.proto.Auth.verify|verify} messages.
                         * @function encode
                         * @memberof com.feige.im.pojo.proto.Auth
                         * @static
                         * @param {com.feige.im.pojo.proto.IAuth} message Auth message or plain object to encode
                         * @param {$protobuf.Writer} [writer] Writer to encode to
                         * @returns {$protobuf.Writer} Writer
                         */
                        Auth.encode = function encode(message, writer) {
                            if (!writer)
                                writer = $Writer.create();
                            if (message.ip != null && Object.hasOwnProperty.call(message, "ip"))
                                writer.uint32(/* id 1, wireType 2 =*/10).string(message.ip);
                            if (message.deviceId != null && Object.hasOwnProperty.call(message, "deviceId"))
                                writer.uint32(/* id 2, wireType 2 =*/18).string(message.deviceId);
                            if (message.deviceName != null && Object.hasOwnProperty.call(message, "deviceName"))
                                writer.uint32(/* id 3, wireType 2 =*/26).string(message.deviceName);
                            if (message.version != null && Object.hasOwnProperty.call(message, "version"))
                                writer.uint32(/* id 4, wireType 2 =*/34).string(message.version);
                            if (message.osVersion != null && Object.hasOwnProperty.call(message, "osVersion"))
                                writer.uint32(/* id 5, wireType 2 =*/42).string(message.osVersion);
                            if (message.language != null && Object.hasOwnProperty.call(message, "language"))
                                writer.uint32(/* id 6, wireType 2 =*/50).string(message.language);
                            if (message.token != null && Object.hasOwnProperty.call(message, "token"))
                                writer.uint32(/* id 7, wireType 2 =*/58).string(message.token);
                            if (message.platform != null && Object.hasOwnProperty.call(message, "platform"))
                                writer.uint32(/* id 8, wireType 2 =*/66).string(message.platform);
                            if (message.address != null && Object.hasOwnProperty.call(message, "address"))
                                writer.uint32(/* id 9, wireType 2 =*/74).string(message.address);
                            return writer;
                        };

                        /**
                         * Encodes the specified Auth message, length delimited. Does not implicitly {@link com.feige.im.pojo.proto.Auth.verify|verify} messages.
                         * @function encodeDelimited
                         * @memberof com.feige.im.pojo.proto.Auth
                         * @static
                         * @param {com.feige.im.pojo.proto.IAuth} message Auth message or plain object to encode
                         * @param {$protobuf.Writer} [writer] Writer to encode to
                         * @returns {$protobuf.Writer} Writer
                         */
                        Auth.encodeDelimited = function encodeDelimited(message, writer) {
                            return this.encode(message, writer).ldelim();
                        };

                        /**
                         * Decodes an Auth message from the specified reader or buffer.
                         * @function decode
                         * @memberof com.feige.im.pojo.proto.Auth
                         * @static
                         * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
                         * @param {number} [length] Message length if known beforehand
                         * @returns {com.feige.im.pojo.proto.Auth} Auth
                         * @throws {Error} If the payload is not a reader or valid buffer
                         * @throws {$protobuf.util.ProtocolError} If required fields are missing
                         */
                        Auth.decode = function decode(reader, length) {
                            if (!(reader instanceof $Reader))
                                reader = $Reader.create(reader);
                            var end = length === undefined ? reader.len : reader.pos + length, message = new $root.com.feige.im.pojo.proto.Auth();
                            while (reader.pos < end) {
                                var tag = reader.uint32();
                                switch (tag >>> 3) {
                                case 1:
                                    message.ip = reader.string();
                                    break;
                                case 2:
                                    message.deviceId = reader.string();
                                    break;
                                case 3:
                                    message.deviceName = reader.string();
                                    break;
                                case 4:
                                    message.version = reader.string();
                                    break;
                                case 5:
                                    message.osVersion = reader.string();
                                    break;
                                case 6:
                                    message.language = reader.string();
                                    break;
                                case 7:
                                    message.token = reader.string();
                                    break;
                                case 8:
                                    message.platform = reader.string();
                                    break;
                                case 9:
                                    message.address = reader.string();
                                    break;
                                default:
                                    reader.skipType(tag & 7);
                                    break;
                                }
                            }
                            return message;
                        };

                        /**
                         * Decodes an Auth message from the specified reader or buffer, length delimited.
                         * @function decodeDelimited
                         * @memberof com.feige.im.pojo.proto.Auth
                         * @static
                         * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
                         * @returns {com.feige.im.pojo.proto.Auth} Auth
                         * @throws {Error} If the payload is not a reader or valid buffer
                         * @throws {$protobuf.util.ProtocolError} If required fields are missing
                         */
                        Auth.decodeDelimited = function decodeDelimited(reader) {
                            if (!(reader instanceof $Reader))
                                reader = new $Reader(reader);
                            return this.decode(reader, reader.uint32());
                        };

                        /**
                         * Verifies an Auth message.
                         * @function verify
                         * @memberof com.feige.im.pojo.proto.Auth
                         * @static
                         * @param {Object.<string,*>} message Plain object to verify
                         * @returns {string|null} `null` if valid, otherwise the reason why it is not
                         */
                        Auth.verify = function verify(message) {
                            if (typeof message !== "object" || message === null)
                                return "object expected";
                            if (message.ip != null && message.hasOwnProperty("ip"))
                                if (!$util.isString(message.ip))
                                    return "ip: string expected";
                            if (message.deviceId != null && message.hasOwnProperty("deviceId"))
                                if (!$util.isString(message.deviceId))
                                    return "deviceId: string expected";
                            if (message.deviceName != null && message.hasOwnProperty("deviceName"))
                                if (!$util.isString(message.deviceName))
                                    return "deviceName: string expected";
                            if (message.version != null && message.hasOwnProperty("version"))
                                if (!$util.isString(message.version))
                                    return "version: string expected";
                            if (message.osVersion != null && message.hasOwnProperty("osVersion"))
                                if (!$util.isString(message.osVersion))
                                    return "osVersion: string expected";
                            if (message.language != null && message.hasOwnProperty("language"))
                                if (!$util.isString(message.language))
                                    return "language: string expected";
                            if (message.token != null && message.hasOwnProperty("token"))
                                if (!$util.isString(message.token))
                                    return "token: string expected";
                            if (message.platform != null && message.hasOwnProperty("platform"))
                                if (!$util.isString(message.platform))
                                    return "platform: string expected";
                            if (message.address != null && message.hasOwnProperty("address"))
                                if (!$util.isString(message.address))
                                    return "address: string expected";
                            return null;
                        };

                        /**
                         * Creates an Auth message from a plain object. Also converts values to their respective internal types.
                         * @function fromObject
                         * @memberof com.feige.im.pojo.proto.Auth
                         * @static
                         * @param {Object.<string,*>} object Plain object
                         * @returns {com.feige.im.pojo.proto.Auth} Auth
                         */
                        Auth.fromObject = function fromObject(object) {
                            if (object instanceof $root.com.feige.im.pojo.proto.Auth)
                                return object;
                            var message = new $root.com.feige.im.pojo.proto.Auth();
                            if (object.ip != null)
                                message.ip = String(object.ip);
                            if (object.deviceId != null)
                                message.deviceId = String(object.deviceId);
                            if (object.deviceName != null)
                                message.deviceName = String(object.deviceName);
                            if (object.version != null)
                                message.version = String(object.version);
                            if (object.osVersion != null)
                                message.osVersion = String(object.osVersion);
                            if (object.language != null)
                                message.language = String(object.language);
                            if (object.token != null)
                                message.token = String(object.token);
                            if (object.platform != null)
                                message.platform = String(object.platform);
                            if (object.address != null)
                                message.address = String(object.address);
                            return message;
                        };

                        /**
                         * Creates a plain object from an Auth message. Also converts values to other types if specified.
                         * @function toObject
                         * @memberof com.feige.im.pojo.proto.Auth
                         * @static
                         * @param {com.feige.im.pojo.proto.Auth} message Auth
                         * @param {$protobuf.IConversionOptions} [options] Conversion options
                         * @returns {Object.<string,*>} Plain object
                         */
                        Auth.toObject = function toObject(message, options) {
                            if (!options)
                                options = {};
                            var object = {};
                            if (options.defaults) {
                                object.ip = "";
                                object.deviceId = "";
                                object.deviceName = "";
                                object.version = "";
                                object.osVersion = "";
                                object.language = "";
                                object.token = "";
                                object.platform = "";
                                object.address = "";
                            }
                            if (message.ip != null && message.hasOwnProperty("ip"))
                                object.ip = message.ip;
                            if (message.deviceId != null && message.hasOwnProperty("deviceId"))
                                object.deviceId = message.deviceId;
                            if (message.deviceName != null && message.hasOwnProperty("deviceName"))
                                object.deviceName = message.deviceName;
                            if (message.version != null && message.hasOwnProperty("version"))
                                object.version = message.version;
                            if (message.osVersion != null && message.hasOwnProperty("osVersion"))
                                object.osVersion = message.osVersion;
                            if (message.language != null && message.hasOwnProperty("language"))
                                object.language = message.language;
                            if (message.token != null && message.hasOwnProperty("token"))
                                object.token = message.token;
                            if (message.platform != null && message.hasOwnProperty("platform"))
                                object.platform = message.platform;
                            if (message.address != null && message.hasOwnProperty("address"))
                                object.address = message.address;
                            return object;
                        };

                        /**
                         * Converts this Auth to JSON.
                         * @function toJSON
                         * @memberof com.feige.im.pojo.proto.Auth
                         * @instance
                         * @returns {Object.<string,*>} JSON object
                         */
                        Auth.prototype.toJSON = function toJSON() {
                            return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
                        };

                        return Auth;
                    })();

                    proto.Forced = (function() {

                        /**
                         * Properties of a Forced.
                         * @memberof com.feige.im.pojo.proto
                         * @interface IForced
                         * @property {string|null} [ip] 账号登录的IP
                         * @property {string|null} [address] 账号登录的地址
                         * @property {string|null} [content] 提示内容
                         * @property {string|null} [deviceName] 设备名
                         * @property {string|null} [timestamp] 时间
                         */

                        /**
                         * Constructs a new Forced.
                         * @memberof com.feige.im.pojo.proto
                         * @classdesc Represents a Forced.
                         * @implements IForced
                         * @constructor
                         * @param {com.feige.im.pojo.proto.IForced=} [properties] Properties to set
                         */
                        function Forced(properties) {
                            if (properties)
                                for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                                    if (properties[keys[i]] != null)
                                        this[keys[i]] = properties[keys[i]];
                        }

                        /**
                         * 账号登录的IP
                         * @member {string} ip
                         * @memberof com.feige.im.pojo.proto.Forced
                         * @instance
                         */
                        Forced.prototype.ip = "";

                        /**
                         * 账号登录的地址
                         * @member {string} address
                         * @memberof com.feige.im.pojo.proto.Forced
                         * @instance
                         */
                        Forced.prototype.address = "";

                        /**
                         * 提示内容
                         * @member {string} content
                         * @memberof com.feige.im.pojo.proto.Forced
                         * @instance
                         */
                        Forced.prototype.content = "";

                        /**
                         * 设备名
                         * @member {string} deviceName
                         * @memberof com.feige.im.pojo.proto.Forced
                         * @instance
                         */
                        Forced.prototype.deviceName = "";

                        /**
                         * 时间
                         * @member {string} timestamp
                         * @memberof com.feige.im.pojo.proto.Forced
                         * @instance
                         */
                        Forced.prototype.timestamp = "";

                        /**
                         * Creates a new Forced instance using the specified properties.
                         * @function create
                         * @memberof com.feige.im.pojo.proto.Forced
                         * @static
                         * @param {com.feige.im.pojo.proto.IForced=} [properties] Properties to set
                         * @returns {com.feige.im.pojo.proto.Forced} Forced instance
                         */
                        Forced.create = function create(properties) {
                            return new Forced(properties);
                        };

                        /**
                         * Encodes the specified Forced message. Does not implicitly {@link com.feige.im.pojo.proto.Forced.verify|verify} messages.
                         * @function encode
                         * @memberof com.feige.im.pojo.proto.Forced
                         * @static
                         * @param {com.feige.im.pojo.proto.IForced} message Forced message or plain object to encode
                         * @param {$protobuf.Writer} [writer] Writer to encode to
                         * @returns {$protobuf.Writer} Writer
                         */
                        Forced.encode = function encode(message, writer) {
                            if (!writer)
                                writer = $Writer.create();
                            if (message.ip != null && Object.hasOwnProperty.call(message, "ip"))
                                writer.uint32(/* id 1, wireType 2 =*/10).string(message.ip);
                            if (message.address != null && Object.hasOwnProperty.call(message, "address"))
                                writer.uint32(/* id 2, wireType 2 =*/18).string(message.address);
                            if (message.content != null && Object.hasOwnProperty.call(message, "content"))
                                writer.uint32(/* id 3, wireType 2 =*/26).string(message.content);
                            if (message.deviceName != null && Object.hasOwnProperty.call(message, "deviceName"))
                                writer.uint32(/* id 4, wireType 2 =*/34).string(message.deviceName);
                            if (message.timestamp != null && Object.hasOwnProperty.call(message, "timestamp"))
                                writer.uint32(/* id 5, wireType 2 =*/42).string(message.timestamp);
                            return writer;
                        };

                        /**
                         * Encodes the specified Forced message, length delimited. Does not implicitly {@link com.feige.im.pojo.proto.Forced.verify|verify} messages.
                         * @function encodeDelimited
                         * @memberof com.feige.im.pojo.proto.Forced
                         * @static
                         * @param {com.feige.im.pojo.proto.IForced} message Forced message or plain object to encode
                         * @param {$protobuf.Writer} [writer] Writer to encode to
                         * @returns {$protobuf.Writer} Writer
                         */
                        Forced.encodeDelimited = function encodeDelimited(message, writer) {
                            return this.encode(message, writer).ldelim();
                        };

                        /**
                         * Decodes a Forced message from the specified reader or buffer.
                         * @function decode
                         * @memberof com.feige.im.pojo.proto.Forced
                         * @static
                         * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
                         * @param {number} [length] Message length if known beforehand
                         * @returns {com.feige.im.pojo.proto.Forced} Forced
                         * @throws {Error} If the payload is not a reader or valid buffer
                         * @throws {$protobuf.util.ProtocolError} If required fields are missing
                         */
                        Forced.decode = function decode(reader, length) {
                            if (!(reader instanceof $Reader))
                                reader = $Reader.create(reader);
                            var end = length === undefined ? reader.len : reader.pos + length, message = new $root.com.feige.im.pojo.proto.Forced();
                            while (reader.pos < end) {
                                var tag = reader.uint32();
                                switch (tag >>> 3) {
                                case 1:
                                    message.ip = reader.string();
                                    break;
                                case 2:
                                    message.address = reader.string();
                                    break;
                                case 3:
                                    message.content = reader.string();
                                    break;
                                case 4:
                                    message.deviceName = reader.string();
                                    break;
                                case 5:
                                    message.timestamp = reader.string();
                                    break;
                                default:
                                    reader.skipType(tag & 7);
                                    break;
                                }
                            }
                            return message;
                        };

                        /**
                         * Decodes a Forced message from the specified reader or buffer, length delimited.
                         * @function decodeDelimited
                         * @memberof com.feige.im.pojo.proto.Forced
                         * @static
                         * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
                         * @returns {com.feige.im.pojo.proto.Forced} Forced
                         * @throws {Error} If the payload is not a reader or valid buffer
                         * @throws {$protobuf.util.ProtocolError} If required fields are missing
                         */
                        Forced.decodeDelimited = function decodeDelimited(reader) {
                            if (!(reader instanceof $Reader))
                                reader = new $Reader(reader);
                            return this.decode(reader, reader.uint32());
                        };

                        /**
                         * Verifies a Forced message.
                         * @function verify
                         * @memberof com.feige.im.pojo.proto.Forced
                         * @static
                         * @param {Object.<string,*>} message Plain object to verify
                         * @returns {string|null} `null` if valid, otherwise the reason why it is not
                         */
                        Forced.verify = function verify(message) {
                            if (typeof message !== "object" || message === null)
                                return "object expected";
                            if (message.ip != null && message.hasOwnProperty("ip"))
                                if (!$util.isString(message.ip))
                                    return "ip: string expected";
                            if (message.address != null && message.hasOwnProperty("address"))
                                if (!$util.isString(message.address))
                                    return "address: string expected";
                            if (message.content != null && message.hasOwnProperty("content"))
                                if (!$util.isString(message.content))
                                    return "content: string expected";
                            if (message.deviceName != null && message.hasOwnProperty("deviceName"))
                                if (!$util.isString(message.deviceName))
                                    return "deviceName: string expected";
                            if (message.timestamp != null && message.hasOwnProperty("timestamp"))
                                if (!$util.isString(message.timestamp))
                                    return "timestamp: string expected";
                            return null;
                        };

                        /**
                         * Creates a Forced message from a plain object. Also converts values to their respective internal types.
                         * @function fromObject
                         * @memberof com.feige.im.pojo.proto.Forced
                         * @static
                         * @param {Object.<string,*>} object Plain object
                         * @returns {com.feige.im.pojo.proto.Forced} Forced
                         */
                        Forced.fromObject = function fromObject(object) {
                            if (object instanceof $root.com.feige.im.pojo.proto.Forced)
                                return object;
                            var message = new $root.com.feige.im.pojo.proto.Forced();
                            if (object.ip != null)
                                message.ip = String(object.ip);
                            if (object.address != null)
                                message.address = String(object.address);
                            if (object.content != null)
                                message.content = String(object.content);
                            if (object.deviceName != null)
                                message.deviceName = String(object.deviceName);
                            if (object.timestamp != null)
                                message.timestamp = String(object.timestamp);
                            return message;
                        };

                        /**
                         * Creates a plain object from a Forced message. Also converts values to other types if specified.
                         * @function toObject
                         * @memberof com.feige.im.pojo.proto.Forced
                         * @static
                         * @param {com.feige.im.pojo.proto.Forced} message Forced
                         * @param {$protobuf.IConversionOptions} [options] Conversion options
                         * @returns {Object.<string,*>} Plain object
                         */
                        Forced.toObject = function toObject(message, options) {
                            if (!options)
                                options = {};
                            var object = {};
                            if (options.defaults) {
                                object.ip = "";
                                object.address = "";
                                object.content = "";
                                object.deviceName = "";
                                object.timestamp = "";
                            }
                            if (message.ip != null && message.hasOwnProperty("ip"))
                                object.ip = message.ip;
                            if (message.address != null && message.hasOwnProperty("address"))
                                object.address = message.address;
                            if (message.content != null && message.hasOwnProperty("content"))
                                object.content = message.content;
                            if (message.deviceName != null && message.hasOwnProperty("deviceName"))
                                object.deviceName = message.deviceName;
                            if (message.timestamp != null && message.hasOwnProperty("timestamp"))
                                object.timestamp = message.timestamp;
                            return object;
                        };

                        /**
                         * Converts this Forced to JSON.
                         * @function toJSON
                         * @memberof com.feige.im.pojo.proto.Forced
                         * @instance
                         * @returns {Object.<string,*>} JSON object
                         */
                        Forced.prototype.toJSON = function toJSON() {
                            return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
                        };

                        return Forced;
                    })();

                    proto.Msg = (function() {

                        /**
                         * Properties of a Msg.
                         * @memberof com.feige.im.pojo.proto
                         * @interface IMsg
                         * @property {number|Long|null} [id] 消息ID
                         * @property {string|null} [senderId] 发送者ID
                         * @property {string|null} [receiverId] 消息接收者ID
                         * @property {string|null} [content] 消息内容
                         * @property {number|null} [format] 消息格式 txt/json/html/...
                         * @property {number|null} [msgType] 消息类型 图片/语音/...
                         * @property {string|null} [timestamp] 时间
                         * @property {number|null} [status] 消息状态
                         * @property {string|null} [extra] 额外消息
                         */

                        /**
                         * Constructs a new Msg.
                         * @memberof com.feige.im.pojo.proto
                         * @classdesc Represents a Msg.
                         * @implements IMsg
                         * @constructor
                         * @param {com.feige.im.pojo.proto.IMsg=} [properties] Properties to set
                         */
                        function Msg(properties) {
                            if (properties)
                                for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                                    if (properties[keys[i]] != null)
                                        this[keys[i]] = properties[keys[i]];
                        }

                        /**
                         * 消息ID
                         * @member {number|Long} id
                         * @memberof com.feige.im.pojo.proto.Msg
                         * @instance
                         */
                        Msg.prototype.id = $util.Long ? $util.Long.fromBits(0,0,false) : 0;

                        /**
                         * 发送者ID
                         * @member {string} senderId
                         * @memberof com.feige.im.pojo.proto.Msg
                         * @instance
                         */
                        Msg.prototype.senderId = "";

                        /**
                         * 消息接收者ID
                         * @member {string} receiverId
                         * @memberof com.feige.im.pojo.proto.Msg
                         * @instance
                         */
                        Msg.prototype.receiverId = "";

                        /**
                         * 消息内容
                         * @member {string} content
                         * @memberof com.feige.im.pojo.proto.Msg
                         * @instance
                         */
                        Msg.prototype.content = "";

                        /**
                         * 消息格式 txt/json/html/...
                         * @member {number} format
                         * @memberof com.feige.im.pojo.proto.Msg
                         * @instance
                         */
                        Msg.prototype.format = 0;

                        /**
                         * 消息类型 图片/语音/...
                         * @member {number} msgType
                         * @memberof com.feige.im.pojo.proto.Msg
                         * @instance
                         */
                        Msg.prototype.msgType = 0;

                        /**
                         * 时间
                         * @member {string} timestamp
                         * @memberof com.feige.im.pojo.proto.Msg
                         * @instance
                         */
                        Msg.prototype.timestamp = "";

                        /**
                         * 消息状态
                         * @member {number} status
                         * @memberof com.feige.im.pojo.proto.Msg
                         * @instance
                         */
                        Msg.prototype.status = 0;

                        /**
                         * 额外消息
                         * @member {string} extra
                         * @memberof com.feige.im.pojo.proto.Msg
                         * @instance
                         */
                        Msg.prototype.extra = "";

                        /**
                         * Creates a new Msg instance using the specified properties.
                         * @function create
                         * @memberof com.feige.im.pojo.proto.Msg
                         * @static
                         * @param {com.feige.im.pojo.proto.IMsg=} [properties] Properties to set
                         * @returns {com.feige.im.pojo.proto.Msg} Msg instance
                         */
                        Msg.create = function create(properties) {
                            return new Msg(properties);
                        };

                        /**
                         * Encodes the specified Msg message. Does not implicitly {@link com.feige.im.pojo.proto.Msg.verify|verify} messages.
                         * @function encode
                         * @memberof com.feige.im.pojo.proto.Msg
                         * @static
                         * @param {com.feige.im.pojo.proto.IMsg} message Msg message or plain object to encode
                         * @param {$protobuf.Writer} [writer] Writer to encode to
                         * @returns {$protobuf.Writer} Writer
                         */
                        Msg.encode = function encode(message, writer) {
                            if (!writer)
                                writer = $Writer.create();
                            if (message.id != null && Object.hasOwnProperty.call(message, "id"))
                                writer.uint32(/* id 1, wireType 0 =*/8).int64(message.id);
                            if (message.senderId != null && Object.hasOwnProperty.call(message, "senderId"))
                                writer.uint32(/* id 2, wireType 2 =*/18).string(message.senderId);
                            if (message.receiverId != null && Object.hasOwnProperty.call(message, "receiverId"))
                                writer.uint32(/* id 3, wireType 2 =*/26).string(message.receiverId);
                            if (message.content != null && Object.hasOwnProperty.call(message, "content"))
                                writer.uint32(/* id 4, wireType 2 =*/34).string(message.content);
                            if (message.format != null && Object.hasOwnProperty.call(message, "format"))
                                writer.uint32(/* id 5, wireType 0 =*/40).int32(message.format);
                            if (message.msgType != null && Object.hasOwnProperty.call(message, "msgType"))
                                writer.uint32(/* id 6, wireType 0 =*/48).uint32(message.msgType);
                            if (message.timestamp != null && Object.hasOwnProperty.call(message, "timestamp"))
                                writer.uint32(/* id 7, wireType 2 =*/58).string(message.timestamp);
                            if (message.status != null && Object.hasOwnProperty.call(message, "status"))
                                writer.uint32(/* id 8, wireType 0 =*/64).uint32(message.status);
                            if (message.extra != null && Object.hasOwnProperty.call(message, "extra"))
                                writer.uint32(/* id 9, wireType 2 =*/74).string(message.extra);
                            return writer;
                        };

                        /**
                         * Encodes the specified Msg message, length delimited. Does not implicitly {@link com.feige.im.pojo.proto.Msg.verify|verify} messages.
                         * @function encodeDelimited
                         * @memberof com.feige.im.pojo.proto.Msg
                         * @static
                         * @param {com.feige.im.pojo.proto.IMsg} message Msg message or plain object to encode
                         * @param {$protobuf.Writer} [writer] Writer to encode to
                         * @returns {$protobuf.Writer} Writer
                         */
                        Msg.encodeDelimited = function encodeDelimited(message, writer) {
                            return this.encode(message, writer).ldelim();
                        };

                        /**
                         * Decodes a Msg message from the specified reader or buffer.
                         * @function decode
                         * @memberof com.feige.im.pojo.proto.Msg
                         * @static
                         * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
                         * @param {number} [length] Message length if known beforehand
                         * @returns {com.feige.im.pojo.proto.Msg} Msg
                         * @throws {Error} If the payload is not a reader or valid buffer
                         * @throws {$protobuf.util.ProtocolError} If required fields are missing
                         */
                        Msg.decode = function decode(reader, length) {
                            if (!(reader instanceof $Reader))
                                reader = $Reader.create(reader);
                            var end = length === undefined ? reader.len : reader.pos + length, message = new $root.com.feige.im.pojo.proto.Msg();
                            while (reader.pos < end) {
                                var tag = reader.uint32();
                                switch (tag >>> 3) {
                                case 1:
                                    message.id = reader.int64();
                                    break;
                                case 2:
                                    message.senderId = reader.string();
                                    break;
                                case 3:
                                    message.receiverId = reader.string();
                                    break;
                                case 4:
                                    message.content = reader.string();
                                    break;
                                case 5:
                                    message.format = reader.int32();
                                    break;
                                case 6:
                                    message.msgType = reader.uint32();
                                    break;
                                case 7:
                                    message.timestamp = reader.string();
                                    break;
                                case 8:
                                    message.status = reader.uint32();
                                    break;
                                case 9:
                                    message.extra = reader.string();
                                    break;
                                default:
                                    reader.skipType(tag & 7);
                                    break;
                                }
                            }
                            return message;
                        };

                        /**
                         * Decodes a Msg message from the specified reader or buffer, length delimited.
                         * @function decodeDelimited
                         * @memberof com.feige.im.pojo.proto.Msg
                         * @static
                         * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
                         * @returns {com.feige.im.pojo.proto.Msg} Msg
                         * @throws {Error} If the payload is not a reader or valid buffer
                         * @throws {$protobuf.util.ProtocolError} If required fields are missing
                         */
                        Msg.decodeDelimited = function decodeDelimited(reader) {
                            if (!(reader instanceof $Reader))
                                reader = new $Reader(reader);
                            return this.decode(reader, reader.uint32());
                        };

                        /**
                         * Verifies a Msg message.
                         * @function verify
                         * @memberof com.feige.im.pojo.proto.Msg
                         * @static
                         * @param {Object.<string,*>} message Plain object to verify
                         * @returns {string|null} `null` if valid, otherwise the reason why it is not
                         */
                        Msg.verify = function verify(message) {
                            if (typeof message !== "object" || message === null)
                                return "object expected";
                            if (message.id != null && message.hasOwnProperty("id"))
                                if (!$util.isInteger(message.id) && !(message.id && $util.isInteger(message.id.low) && $util.isInteger(message.id.high)))
                                    return "id: integer|Long expected";
                            if (message.senderId != null && message.hasOwnProperty("senderId"))
                                if (!$util.isString(message.senderId))
                                    return "senderId: string expected";
                            if (message.receiverId != null && message.hasOwnProperty("receiverId"))
                                if (!$util.isString(message.receiverId))
                                    return "receiverId: string expected";
                            if (message.content != null && message.hasOwnProperty("content"))
                                if (!$util.isString(message.content))
                                    return "content: string expected";
                            if (message.format != null && message.hasOwnProperty("format"))
                                if (!$util.isInteger(message.format))
                                    return "format: integer expected";
                            if (message.msgType != null && message.hasOwnProperty("msgType"))
                                if (!$util.isInteger(message.msgType))
                                    return "msgType: integer expected";
                            if (message.timestamp != null && message.hasOwnProperty("timestamp"))
                                if (!$util.isString(message.timestamp))
                                    return "timestamp: string expected";
                            if (message.status != null && message.hasOwnProperty("status"))
                                if (!$util.isInteger(message.status))
                                    return "status: integer expected";
                            if (message.extra != null && message.hasOwnProperty("extra"))
                                if (!$util.isString(message.extra))
                                    return "extra: string expected";
                            return null;
                        };

                        /**
                         * Creates a Msg message from a plain object. Also converts values to their respective internal types.
                         * @function fromObject
                         * @memberof com.feige.im.pojo.proto.Msg
                         * @static
                         * @param {Object.<string,*>} object Plain object
                         * @returns {com.feige.im.pojo.proto.Msg} Msg
                         */
                        Msg.fromObject = function fromObject(object) {
                            if (object instanceof $root.com.feige.im.pojo.proto.Msg)
                                return object;
                            var message = new $root.com.feige.im.pojo.proto.Msg();
                            if (object.id != null)
                                if ($util.Long)
                                    (message.id = $util.Long.fromValue(object.id)).unsigned = false;
                                else if (typeof object.id === "string")
                                    message.id = parseInt(object.id, 10);
                                else if (typeof object.id === "number")
                                    message.id = object.id;
                                else if (typeof object.id === "object")
                                    message.id = new $util.LongBits(object.id.low >>> 0, object.id.high >>> 0).toNumber();
                            if (object.senderId != null)
                                message.senderId = String(object.senderId);
                            if (object.receiverId != null)
                                message.receiverId = String(object.receiverId);
                            if (object.content != null)
                                message.content = String(object.content);
                            if (object.format != null)
                                message.format = object.format | 0;
                            if (object.msgType != null)
                                message.msgType = object.msgType >>> 0;
                            if (object.timestamp != null)
                                message.timestamp = String(object.timestamp);
                            if (object.status != null)
                                message.status = object.status >>> 0;
                            if (object.extra != null)
                                message.extra = String(object.extra);
                            return message;
                        };

                        /**
                         * Creates a plain object from a Msg message. Also converts values to other types if specified.
                         * @function toObject
                         * @memberof com.feige.im.pojo.proto.Msg
                         * @static
                         * @param {com.feige.im.pojo.proto.Msg} message Msg
                         * @param {$protobuf.IConversionOptions} [options] Conversion options
                         * @returns {Object.<string,*>} Plain object
                         */
                        Msg.toObject = function toObject(message, options) {
                            if (!options)
                                options = {};
                            var object = {};
                            if (options.defaults) {
                                if ($util.Long) {
                                    var long = new $util.Long(0, 0, false);
                                    object.id = options.longs === String ? long.toString() : options.longs === Number ? long.toNumber() : long;
                                } else
                                    object.id = options.longs === String ? "0" : 0;
                                object.senderId = "";
                                object.receiverId = "";
                                object.content = "";
                                object.format = 0;
                                object.msgType = 0;
                                object.timestamp = "";
                                object.status = 0;
                                object.extra = "";
                            }
                            if (message.id != null && message.hasOwnProperty("id"))
                                if (typeof message.id === "number")
                                    object.id = options.longs === String ? String(message.id) : message.id;
                                else
                                    object.id = options.longs === String ? $util.Long.prototype.toString.call(message.id) : options.longs === Number ? new $util.LongBits(message.id.low >>> 0, message.id.high >>> 0).toNumber() : message.id;
                            if (message.senderId != null && message.hasOwnProperty("senderId"))
                                object.senderId = message.senderId;
                            if (message.receiverId != null && message.hasOwnProperty("receiverId"))
                                object.receiverId = message.receiverId;
                            if (message.content != null && message.hasOwnProperty("content"))
                                object.content = message.content;
                            if (message.format != null && message.hasOwnProperty("format"))
                                object.format = message.format;
                            if (message.msgType != null && message.hasOwnProperty("msgType"))
                                object.msgType = message.msgType;
                            if (message.timestamp != null && message.hasOwnProperty("timestamp"))
                                object.timestamp = message.timestamp;
                            if (message.status != null && message.hasOwnProperty("status"))
                                object.status = message.status;
                            if (message.extra != null && message.hasOwnProperty("extra"))
                                object.extra = message.extra;
                            return object;
                        };

                        /**
                         * Converts this Msg to JSON.
                         * @function toJSON
                         * @memberof com.feige.im.pojo.proto.Msg
                         * @instance
                         * @returns {Object.<string,*>} JSON object
                         */
                        Msg.prototype.toJSON = function toJSON() {
                            return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
                        };

                        return Msg;
                    })();

                    proto.TransportMsg = (function() {

                        /**
                         * Properties of a TransportMsg.
                         * @memberof com.feige.im.pojo.proto
                         * @interface ITransportMsg
                         * @property {number|null} [type] TransportMsg type
                         * @property {com.feige.im.pojo.proto.IMsg|null} [msg] TransportMsg msg
                         */

                        /**
                         * Constructs a new TransportMsg.
                         * @memberof com.feige.im.pojo.proto
                         * @classdesc 传输消息
                         * type 1私聊消息2群聊消息
                         * @implements ITransportMsg
                         * @constructor
                         * @param {com.feige.im.pojo.proto.ITransportMsg=} [properties] Properties to set
                         */
                        function TransportMsg(properties) {
                            if (properties)
                                for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                                    if (properties[keys[i]] != null)
                                        this[keys[i]] = properties[keys[i]];
                        }

                        /**
                         * TransportMsg type.
                         * @member {number} type
                         * @memberof com.feige.im.pojo.proto.TransportMsg
                         * @instance
                         */
                        TransportMsg.prototype.type = 0;

                        /**
                         * TransportMsg msg.
                         * @member {com.feige.im.pojo.proto.IMsg|null|undefined} msg
                         * @memberof com.feige.im.pojo.proto.TransportMsg
                         * @instance
                         */
                        TransportMsg.prototype.msg = null;

                        /**
                         * Creates a new TransportMsg instance using the specified properties.
                         * @function create
                         * @memberof com.feige.im.pojo.proto.TransportMsg
                         * @static
                         * @param {com.feige.im.pojo.proto.ITransportMsg=} [properties] Properties to set
                         * @returns {com.feige.im.pojo.proto.TransportMsg} TransportMsg instance
                         */
                        TransportMsg.create = function create(properties) {
                            return new TransportMsg(properties);
                        };

                        /**
                         * Encodes the specified TransportMsg message. Does not implicitly {@link com.feige.im.pojo.proto.TransportMsg.verify|verify} messages.
                         * @function encode
                         * @memberof com.feige.im.pojo.proto.TransportMsg
                         * @static
                         * @param {com.feige.im.pojo.proto.ITransportMsg} message TransportMsg message or plain object to encode
                         * @param {$protobuf.Writer} [writer] Writer to encode to
                         * @returns {$protobuf.Writer} Writer
                         */
                        TransportMsg.encode = function encode(message, writer) {
                            if (!writer)
                                writer = $Writer.create();
                            if (message.type != null && Object.hasOwnProperty.call(message, "type"))
                                writer.uint32(/* id 1, wireType 0 =*/8).uint32(message.type);
                            if (message.msg != null && Object.hasOwnProperty.call(message, "msg"))
                                $root.com.feige.im.pojo.proto.Msg.encode(message.msg, writer.uint32(/* id 2, wireType 2 =*/18).fork()).ldelim();
                            return writer;
                        };

                        /**
                         * Encodes the specified TransportMsg message, length delimited. Does not implicitly {@link com.feige.im.pojo.proto.TransportMsg.verify|verify} messages.
                         * @function encodeDelimited
                         * @memberof com.feige.im.pojo.proto.TransportMsg
                         * @static
                         * @param {com.feige.im.pojo.proto.ITransportMsg} message TransportMsg message or plain object to encode
                         * @param {$protobuf.Writer} [writer] Writer to encode to
                         * @returns {$protobuf.Writer} Writer
                         */
                        TransportMsg.encodeDelimited = function encodeDelimited(message, writer) {
                            return this.encode(message, writer).ldelim();
                        };

                        /**
                         * Decodes a TransportMsg message from the specified reader or buffer.
                         * @function decode
                         * @memberof com.feige.im.pojo.proto.TransportMsg
                         * @static
                         * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
                         * @param {number} [length] Message length if known beforehand
                         * @returns {com.feige.im.pojo.proto.TransportMsg} TransportMsg
                         * @throws {Error} If the payload is not a reader or valid buffer
                         * @throws {$protobuf.util.ProtocolError} If required fields are missing
                         */
                        TransportMsg.decode = function decode(reader, length) {
                            if (!(reader instanceof $Reader))
                                reader = $Reader.create(reader);
                            var end = length === undefined ? reader.len : reader.pos + length, message = new $root.com.feige.im.pojo.proto.TransportMsg();
                            while (reader.pos < end) {
                                var tag = reader.uint32();
                                switch (tag >>> 3) {
                                case 1:
                                    message.type = reader.uint32();
                                    break;
                                case 2:
                                    message.msg = $root.com.feige.im.pojo.proto.Msg.decode(reader, reader.uint32());
                                    break;
                                default:
                                    reader.skipType(tag & 7);
                                    break;
                                }
                            }
                            return message;
                        };

                        /**
                         * Decodes a TransportMsg message from the specified reader or buffer, length delimited.
                         * @function decodeDelimited
                         * @memberof com.feige.im.pojo.proto.TransportMsg
                         * @static
                         * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
                         * @returns {com.feige.im.pojo.proto.TransportMsg} TransportMsg
                         * @throws {Error} If the payload is not a reader or valid buffer
                         * @throws {$protobuf.util.ProtocolError} If required fields are missing
                         */
                        TransportMsg.decodeDelimited = function decodeDelimited(reader) {
                            if (!(reader instanceof $Reader))
                                reader = new $Reader(reader);
                            return this.decode(reader, reader.uint32());
                        };

                        /**
                         * Verifies a TransportMsg message.
                         * @function verify
                         * @memberof com.feige.im.pojo.proto.TransportMsg
                         * @static
                         * @param {Object.<string,*>} message Plain object to verify
                         * @returns {string|null} `null` if valid, otherwise the reason why it is not
                         */
                        TransportMsg.verify = function verify(message) {
                            if (typeof message !== "object" || message === null)
                                return "object expected";
                            if (message.type != null && message.hasOwnProperty("type"))
                                if (!$util.isInteger(message.type))
                                    return "type: integer expected";
                            if (message.msg != null && message.hasOwnProperty("msg")) {
                                var error = $root.com.feige.im.pojo.proto.Msg.verify(message.msg);
                                if (error)
                                    return "msg." + error;
                            }
                            return null;
                        };

                        /**
                         * Creates a TransportMsg message from a plain object. Also converts values to their respective internal types.
                         * @function fromObject
                         * @memberof com.feige.im.pojo.proto.TransportMsg
                         * @static
                         * @param {Object.<string,*>} object Plain object
                         * @returns {com.feige.im.pojo.proto.TransportMsg} TransportMsg
                         */
                        TransportMsg.fromObject = function fromObject(object) {
                            if (object instanceof $root.com.feige.im.pojo.proto.TransportMsg)
                                return object;
                            var message = new $root.com.feige.im.pojo.proto.TransportMsg();
                            if (object.type != null)
                                message.type = object.type >>> 0;
                            if (object.msg != null) {
                                if (typeof object.msg !== "object")
                                    throw TypeError(".com.feige.im.pojo.proto.TransportMsg.msg: object expected");
                                message.msg = $root.com.feige.im.pojo.proto.Msg.fromObject(object.msg);
                            }
                            return message;
                        };

                        /**
                         * Creates a plain object from a TransportMsg message. Also converts values to other types if specified.
                         * @function toObject
                         * @memberof com.feige.im.pojo.proto.TransportMsg
                         * @static
                         * @param {com.feige.im.pojo.proto.TransportMsg} message TransportMsg
                         * @param {$protobuf.IConversionOptions} [options] Conversion options
                         * @returns {Object.<string,*>} Plain object
                         */
                        TransportMsg.toObject = function toObject(message, options) {
                            if (!options)
                                options = {};
                            var object = {};
                            if (options.defaults) {
                                object.type = 0;
                                object.msg = null;
                            }
                            if (message.type != null && message.hasOwnProperty("type"))
                                object.type = message.type;
                            if (message.msg != null && message.hasOwnProperty("msg"))
                                object.msg = $root.com.feige.im.pojo.proto.Msg.toObject(message.msg, options);
                            return object;
                        };

                        /**
                         * Converts this TransportMsg to JSON.
                         * @function toJSON
                         * @memberof com.feige.im.pojo.proto.TransportMsg
                         * @instance
                         * @returns {Object.<string,*>} JSON object
                         */
                        TransportMsg.prototype.toJSON = function toJSON() {
                            return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
                        };

                        return TransportMsg;
                    })();

                    proto.Ping = (function() {

                        /**
                         * Properties of a Ping.
                         * @memberof com.feige.im.pojo.proto
                         * @interface IPing
                         * @property {number|null} [ping] Ping ping
                         */

                        /**
                         * Constructs a new Ping.
                         * @memberof com.feige.im.pojo.proto
                         * @classdesc 心跳消息
                         * @implements IPing
                         * @constructor
                         * @param {com.feige.im.pojo.proto.IPing=} [properties] Properties to set
                         */
                        function Ping(properties) {
                            if (properties)
                                for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                                    if (properties[keys[i]] != null)
                                        this[keys[i]] = properties[keys[i]];
                        }

                        /**
                         * Ping ping.
                         * @member {number} ping
                         * @memberof com.feige.im.pojo.proto.Ping
                         * @instance
                         */
                        Ping.prototype.ping = 0;

                        /**
                         * Creates a new Ping instance using the specified properties.
                         * @function create
                         * @memberof com.feige.im.pojo.proto.Ping
                         * @static
                         * @param {com.feige.im.pojo.proto.IPing=} [properties] Properties to set
                         * @returns {com.feige.im.pojo.proto.Ping} Ping instance
                         */
                        Ping.create = function create(properties) {
                            return new Ping(properties);
                        };

                        /**
                         * Encodes the specified Ping message. Does not implicitly {@link com.feige.im.pojo.proto.Ping.verify|verify} messages.
                         * @function encode
                         * @memberof com.feige.im.pojo.proto.Ping
                         * @static
                         * @param {com.feige.im.pojo.proto.IPing} message Ping message or plain object to encode
                         * @param {$protobuf.Writer} [writer] Writer to encode to
                         * @returns {$protobuf.Writer} Writer
                         */
                        Ping.encode = function encode(message, writer) {
                            if (!writer)
                                writer = $Writer.create();
                            if (message.ping != null && Object.hasOwnProperty.call(message, "ping"))
                                writer.uint32(/* id 1, wireType 0 =*/8).int32(message.ping);
                            return writer;
                        };

                        /**
                         * Encodes the specified Ping message, length delimited. Does not implicitly {@link com.feige.im.pojo.proto.Ping.verify|verify} messages.
                         * @function encodeDelimited
                         * @memberof com.feige.im.pojo.proto.Ping
                         * @static
                         * @param {com.feige.im.pojo.proto.IPing} message Ping message or plain object to encode
                         * @param {$protobuf.Writer} [writer] Writer to encode to
                         * @returns {$protobuf.Writer} Writer
                         */
                        Ping.encodeDelimited = function encodeDelimited(message, writer) {
                            return this.encode(message, writer).ldelim();
                        };

                        /**
                         * Decodes a Ping message from the specified reader or buffer.
                         * @function decode
                         * @memberof com.feige.im.pojo.proto.Ping
                         * @static
                         * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
                         * @param {number} [length] Message length if known beforehand
                         * @returns {com.feige.im.pojo.proto.Ping} Ping
                         * @throws {Error} If the payload is not a reader or valid buffer
                         * @throws {$protobuf.util.ProtocolError} If required fields are missing
                         */
                        Ping.decode = function decode(reader, length) {
                            if (!(reader instanceof $Reader))
                                reader = $Reader.create(reader);
                            var end = length === undefined ? reader.len : reader.pos + length, message = new $root.com.feige.im.pojo.proto.Ping();
                            while (reader.pos < end) {
                                var tag = reader.uint32();
                                switch (tag >>> 3) {
                                case 1:
                                    message.ping = reader.int32();
                                    break;
                                default:
                                    reader.skipType(tag & 7);
                                    break;
                                }
                            }
                            return message;
                        };

                        /**
                         * Decodes a Ping message from the specified reader or buffer, length delimited.
                         * @function decodeDelimited
                         * @memberof com.feige.im.pojo.proto.Ping
                         * @static
                         * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
                         * @returns {com.feige.im.pojo.proto.Ping} Ping
                         * @throws {Error} If the payload is not a reader or valid buffer
                         * @throws {$protobuf.util.ProtocolError} If required fields are missing
                         */
                        Ping.decodeDelimited = function decodeDelimited(reader) {
                            if (!(reader instanceof $Reader))
                                reader = new $Reader(reader);
                            return this.decode(reader, reader.uint32());
                        };

                        /**
                         * Verifies a Ping message.
                         * @function verify
                         * @memberof com.feige.im.pojo.proto.Ping
                         * @static
                         * @param {Object.<string,*>} message Plain object to verify
                         * @returns {string|null} `null` if valid, otherwise the reason why it is not
                         */
                        Ping.verify = function verify(message) {
                            if (typeof message !== "object" || message === null)
                                return "object expected";
                            if (message.ping != null && message.hasOwnProperty("ping"))
                                if (!$util.isInteger(message.ping))
                                    return "ping: integer expected";
                            return null;
                        };

                        /**
                         * Creates a Ping message from a plain object. Also converts values to their respective internal types.
                         * @function fromObject
                         * @memberof com.feige.im.pojo.proto.Ping
                         * @static
                         * @param {Object.<string,*>} object Plain object
                         * @returns {com.feige.im.pojo.proto.Ping} Ping
                         */
                        Ping.fromObject = function fromObject(object) {
                            if (object instanceof $root.com.feige.im.pojo.proto.Ping)
                                return object;
                            var message = new $root.com.feige.im.pojo.proto.Ping();
                            if (object.ping != null)
                                message.ping = object.ping | 0;
                            return message;
                        };

                        /**
                         * Creates a plain object from a Ping message. Also converts values to other types if specified.
                         * @function toObject
                         * @memberof com.feige.im.pojo.proto.Ping
                         * @static
                         * @param {com.feige.im.pojo.proto.Ping} message Ping
                         * @param {$protobuf.IConversionOptions} [options] Conversion options
                         * @returns {Object.<string,*>} Plain object
                         */
                        Ping.toObject = function toObject(message, options) {
                            if (!options)
                                options = {};
                            var object = {};
                            if (options.defaults)
                                object.ping = 0;
                            if (message.ping != null && message.hasOwnProperty("ping"))
                                object.ping = message.ping;
                            return object;
                        };

                        /**
                         * Converts this Ping to JSON.
                         * @function toJSON
                         * @memberof com.feige.im.pojo.proto.Ping
                         * @instance
                         * @returns {Object.<string,*>} JSON object
                         */
                        Ping.prototype.toJSON = function toJSON() {
                            return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
                        };

                        return Ping;
                    })();

                    proto.Pong = (function() {

                        /**
                         * Properties of a Pong.
                         * @memberof com.feige.im.pojo.proto
                         * @interface IPong
                         * @property {number|null} [pong] Pong pong
                         */

                        /**
                         * Constructs a new Pong.
                         * @memberof com.feige.im.pojo.proto
                         * @classdesc Represents a Pong.
                         * @implements IPong
                         * @constructor
                         * @param {com.feige.im.pojo.proto.IPong=} [properties] Properties to set
                         */
                        function Pong(properties) {
                            if (properties)
                                for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                                    if (properties[keys[i]] != null)
                                        this[keys[i]] = properties[keys[i]];
                        }

                        /**
                         * Pong pong.
                         * @member {number} pong
                         * @memberof com.feige.im.pojo.proto.Pong
                         * @instance
                         */
                        Pong.prototype.pong = 0;

                        /**
                         * Creates a new Pong instance using the specified properties.
                         * @function create
                         * @memberof com.feige.im.pojo.proto.Pong
                         * @static
                         * @param {com.feige.im.pojo.proto.IPong=} [properties] Properties to set
                         * @returns {com.feige.im.pojo.proto.Pong} Pong instance
                         */
                        Pong.create = function create(properties) {
                            return new Pong(properties);
                        };

                        /**
                         * Encodes the specified Pong message. Does not implicitly {@link com.feige.im.pojo.proto.Pong.verify|verify} messages.
                         * @function encode
                         * @memberof com.feige.im.pojo.proto.Pong
                         * @static
                         * @param {com.feige.im.pojo.proto.IPong} message Pong message or plain object to encode
                         * @param {$protobuf.Writer} [writer] Writer to encode to
                         * @returns {$protobuf.Writer} Writer
                         */
                        Pong.encode = function encode(message, writer) {
                            if (!writer)
                                writer = $Writer.create();
                            if (message.pong != null && Object.hasOwnProperty.call(message, "pong"))
                                writer.uint32(/* id 1, wireType 0 =*/8).uint32(message.pong);
                            return writer;
                        };

                        /**
                         * Encodes the specified Pong message, length delimited. Does not implicitly {@link com.feige.im.pojo.proto.Pong.verify|verify} messages.
                         * @function encodeDelimited
                         * @memberof com.feige.im.pojo.proto.Pong
                         * @static
                         * @param {com.feige.im.pojo.proto.IPong} message Pong message or plain object to encode
                         * @param {$protobuf.Writer} [writer] Writer to encode to
                         * @returns {$protobuf.Writer} Writer
                         */
                        Pong.encodeDelimited = function encodeDelimited(message, writer) {
                            return this.encode(message, writer).ldelim();
                        };

                        /**
                         * Decodes a Pong message from the specified reader or buffer.
                         * @function decode
                         * @memberof com.feige.im.pojo.proto.Pong
                         * @static
                         * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
                         * @param {number} [length] Message length if known beforehand
                         * @returns {com.feige.im.pojo.proto.Pong} Pong
                         * @throws {Error} If the payload is not a reader or valid buffer
                         * @throws {$protobuf.util.ProtocolError} If required fields are missing
                         */
                        Pong.decode = function decode(reader, length) {
                            if (!(reader instanceof $Reader))
                                reader = $Reader.create(reader);
                            var end = length === undefined ? reader.len : reader.pos + length, message = new $root.com.feige.im.pojo.proto.Pong();
                            while (reader.pos < end) {
                                var tag = reader.uint32();
                                switch (tag >>> 3) {
                                case 1:
                                    message.pong = reader.uint32();
                                    break;
                                default:
                                    reader.skipType(tag & 7);
                                    break;
                                }
                            }
                            return message;
                        };

                        /**
                         * Decodes a Pong message from the specified reader or buffer, length delimited.
                         * @function decodeDelimited
                         * @memberof com.feige.im.pojo.proto.Pong
                         * @static
                         * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
                         * @returns {com.feige.im.pojo.proto.Pong} Pong
                         * @throws {Error} If the payload is not a reader or valid buffer
                         * @throws {$protobuf.util.ProtocolError} If required fields are missing
                         */
                        Pong.decodeDelimited = function decodeDelimited(reader) {
                            if (!(reader instanceof $Reader))
                                reader = new $Reader(reader);
                            return this.decode(reader, reader.uint32());
                        };

                        /**
                         * Verifies a Pong message.
                         * @function verify
                         * @memberof com.feige.im.pojo.proto.Pong
                         * @static
                         * @param {Object.<string,*>} message Plain object to verify
                         * @returns {string|null} `null` if valid, otherwise the reason why it is not
                         */
                        Pong.verify = function verify(message) {
                            if (typeof message !== "object" || message === null)
                                return "object expected";
                            if (message.pong != null && message.hasOwnProperty("pong"))
                                if (!$util.isInteger(message.pong))
                                    return "pong: integer expected";
                            return null;
                        };

                        /**
                         * Creates a Pong message from a plain object. Also converts values to their respective internal types.
                         * @function fromObject
                         * @memberof com.feige.im.pojo.proto.Pong
                         * @static
                         * @param {Object.<string,*>} object Plain object
                         * @returns {com.feige.im.pojo.proto.Pong} Pong
                         */
                        Pong.fromObject = function fromObject(object) {
                            if (object instanceof $root.com.feige.im.pojo.proto.Pong)
                                return object;
                            var message = new $root.com.feige.im.pojo.proto.Pong();
                            if (object.pong != null)
                                message.pong = object.pong >>> 0;
                            return message;
                        };

                        /**
                         * Creates a plain object from a Pong message. Also converts values to other types if specified.
                         * @function toObject
                         * @memberof com.feige.im.pojo.proto.Pong
                         * @static
                         * @param {com.feige.im.pojo.proto.Pong} message Pong
                         * @param {$protobuf.IConversionOptions} [options] Conversion options
                         * @returns {Object.<string,*>} Plain object
                         */
                        Pong.toObject = function toObject(message, options) {
                            if (!options)
                                options = {};
                            var object = {};
                            if (options.defaults)
                                object.pong = 0;
                            if (message.pong != null && message.hasOwnProperty("pong"))
                                object.pong = message.pong;
                            return object;
                        };

                        /**
                         * Converts this Pong to JSON.
                         * @function toJSON
                         * @memberof com.feige.im.pojo.proto.Pong
                         * @instance
                         * @returns {Object.<string,*>} JSON object
                         */
                        Pong.prototype.toJSON = function toJSON() {
                            return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
                        };

                        return Pong;
                    })();

                    proto.ClusterAuth = (function() {

                        /**
                         * Properties of a ClusterAuth.
                         * @memberof com.feige.im.pojo.proto
                         * @interface IClusterAuth
                         * @property {string|null} [nodeKey] ClusterAuth nodeKey
                         */

                        /**
                         * Constructs a new ClusterAuth.
                         * @memberof com.feige.im.pojo.proto
                         * @classdesc Represents a ClusterAuth.
                         * @implements IClusterAuth
                         * @constructor
                         * @param {com.feige.im.pojo.proto.IClusterAuth=} [properties] Properties to set
                         */
                        function ClusterAuth(properties) {
                            if (properties)
                                for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                                    if (properties[keys[i]] != null)
                                        this[keys[i]] = properties[keys[i]];
                        }

                        /**
                         * ClusterAuth nodeKey.
                         * @member {string} nodeKey
                         * @memberof com.feige.im.pojo.proto.ClusterAuth
                         * @instance
                         */
                        ClusterAuth.prototype.nodeKey = "";

                        /**
                         * Creates a new ClusterAuth instance using the specified properties.
                         * @function create
                         * @memberof com.feige.im.pojo.proto.ClusterAuth
                         * @static
                         * @param {com.feige.im.pojo.proto.IClusterAuth=} [properties] Properties to set
                         * @returns {com.feige.im.pojo.proto.ClusterAuth} ClusterAuth instance
                         */
                        ClusterAuth.create = function create(properties) {
                            return new ClusterAuth(properties);
                        };

                        /**
                         * Encodes the specified ClusterAuth message. Does not implicitly {@link com.feige.im.pojo.proto.ClusterAuth.verify|verify} messages.
                         * @function encode
                         * @memberof com.feige.im.pojo.proto.ClusterAuth
                         * @static
                         * @param {com.feige.im.pojo.proto.IClusterAuth} message ClusterAuth message or plain object to encode
                         * @param {$protobuf.Writer} [writer] Writer to encode to
                         * @returns {$protobuf.Writer} Writer
                         */
                        ClusterAuth.encode = function encode(message, writer) {
                            if (!writer)
                                writer = $Writer.create();
                            if (message.nodeKey != null && Object.hasOwnProperty.call(message, "nodeKey"))
                                writer.uint32(/* id 1, wireType 2 =*/10).string(message.nodeKey);
                            return writer;
                        };

                        /**
                         * Encodes the specified ClusterAuth message, length delimited. Does not implicitly {@link com.feige.im.pojo.proto.ClusterAuth.verify|verify} messages.
                         * @function encodeDelimited
                         * @memberof com.feige.im.pojo.proto.ClusterAuth
                         * @static
                         * @param {com.feige.im.pojo.proto.IClusterAuth} message ClusterAuth message or plain object to encode
                         * @param {$protobuf.Writer} [writer] Writer to encode to
                         * @returns {$protobuf.Writer} Writer
                         */
                        ClusterAuth.encodeDelimited = function encodeDelimited(message, writer) {
                            return this.encode(message, writer).ldelim();
                        };

                        /**
                         * Decodes a ClusterAuth message from the specified reader or buffer.
                         * @function decode
                         * @memberof com.feige.im.pojo.proto.ClusterAuth
                         * @static
                         * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
                         * @param {number} [length] Message length if known beforehand
                         * @returns {com.feige.im.pojo.proto.ClusterAuth} ClusterAuth
                         * @throws {Error} If the payload is not a reader or valid buffer
                         * @throws {$protobuf.util.ProtocolError} If required fields are missing
                         */
                        ClusterAuth.decode = function decode(reader, length) {
                            if (!(reader instanceof $Reader))
                                reader = $Reader.create(reader);
                            var end = length === undefined ? reader.len : reader.pos + length, message = new $root.com.feige.im.pojo.proto.ClusterAuth();
                            while (reader.pos < end) {
                                var tag = reader.uint32();
                                switch (tag >>> 3) {
                                case 1:
                                    message.nodeKey = reader.string();
                                    break;
                                default:
                                    reader.skipType(tag & 7);
                                    break;
                                }
                            }
                            return message;
                        };

                        /**
                         * Decodes a ClusterAuth message from the specified reader or buffer, length delimited.
                         * @function decodeDelimited
                         * @memberof com.feige.im.pojo.proto.ClusterAuth
                         * @static
                         * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
                         * @returns {com.feige.im.pojo.proto.ClusterAuth} ClusterAuth
                         * @throws {Error} If the payload is not a reader or valid buffer
                         * @throws {$protobuf.util.ProtocolError} If required fields are missing
                         */
                        ClusterAuth.decodeDelimited = function decodeDelimited(reader) {
                            if (!(reader instanceof $Reader))
                                reader = new $Reader(reader);
                            return this.decode(reader, reader.uint32());
                        };

                        /**
                         * Verifies a ClusterAuth message.
                         * @function verify
                         * @memberof com.feige.im.pojo.proto.ClusterAuth
                         * @static
                         * @param {Object.<string,*>} message Plain object to verify
                         * @returns {string|null} `null` if valid, otherwise the reason why it is not
                         */
                        ClusterAuth.verify = function verify(message) {
                            if (typeof message !== "object" || message === null)
                                return "object expected";
                            if (message.nodeKey != null && message.hasOwnProperty("nodeKey"))
                                if (!$util.isString(message.nodeKey))
                                    return "nodeKey: string expected";
                            return null;
                        };

                        /**
                         * Creates a ClusterAuth message from a plain object. Also converts values to their respective internal types.
                         * @function fromObject
                         * @memberof com.feige.im.pojo.proto.ClusterAuth
                         * @static
                         * @param {Object.<string,*>} object Plain object
                         * @returns {com.feige.im.pojo.proto.ClusterAuth} ClusterAuth
                         */
                        ClusterAuth.fromObject = function fromObject(object) {
                            if (object instanceof $root.com.feige.im.pojo.proto.ClusterAuth)
                                return object;
                            var message = new $root.com.feige.im.pojo.proto.ClusterAuth();
                            if (object.nodeKey != null)
                                message.nodeKey = String(object.nodeKey);
                            return message;
                        };

                        /**
                         * Creates a plain object from a ClusterAuth message. Also converts values to other types if specified.
                         * @function toObject
                         * @memberof com.feige.im.pojo.proto.ClusterAuth
                         * @static
                         * @param {com.feige.im.pojo.proto.ClusterAuth} message ClusterAuth
                         * @param {$protobuf.IConversionOptions} [options] Conversion options
                         * @returns {Object.<string,*>} Plain object
                         */
                        ClusterAuth.toObject = function toObject(message, options) {
                            if (!options)
                                options = {};
                            var object = {};
                            if (options.defaults)
                                object.nodeKey = "";
                            if (message.nodeKey != null && message.hasOwnProperty("nodeKey"))
                                object.nodeKey = message.nodeKey;
                            return object;
                        };

                        /**
                         * Converts this ClusterAuth to JSON.
                         * @function toJSON
                         * @memberof com.feige.im.pojo.proto.ClusterAuth
                         * @instance
                         * @returns {Object.<string,*>} JSON object
                         */
                        ClusterAuth.prototype.toJSON = function toJSON() {
                            return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
                        };

                        return ClusterAuth;
                    })();

                    proto.Ack = (function() {

                        /**
                         * Properties of an Ack.
                         * @memberof com.feige.im.pojo.proto
                         * @interface IAck
                         * @property {string|null} [id] Ack id
                         * @property {string|null} [status] Ack status
                         */

                        /**
                         * Constructs a new Ack.
                         * @memberof com.feige.im.pojo.proto
                         * @classdesc Represents an Ack.
                         * @implements IAck
                         * @constructor
                         * @param {com.feige.im.pojo.proto.IAck=} [properties] Properties to set
                         */
                        function Ack(properties) {
                            if (properties)
                                for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
                                    if (properties[keys[i]] != null)
                                        this[keys[i]] = properties[keys[i]];
                        }

                        /**
                         * Ack id.
                         * @member {string} id
                         * @memberof com.feige.im.pojo.proto.Ack
                         * @instance
                         */
                        Ack.prototype.id = "";

                        /**
                         * Ack status.
                         * @member {string} status
                         * @memberof com.feige.im.pojo.proto.Ack
                         * @instance
                         */
                        Ack.prototype.status = "";

                        /**
                         * Creates a new Ack instance using the specified properties.
                         * @function create
                         * @memberof com.feige.im.pojo.proto.Ack
                         * @static
                         * @param {com.feige.im.pojo.proto.IAck=} [properties] Properties to set
                         * @returns {com.feige.im.pojo.proto.Ack} Ack instance
                         */
                        Ack.create = function create(properties) {
                            return new Ack(properties);
                        };

                        /**
                         * Encodes the specified Ack message. Does not implicitly {@link com.feige.im.pojo.proto.Ack.verify|verify} messages.
                         * @function encode
                         * @memberof com.feige.im.pojo.proto.Ack
                         * @static
                         * @param {com.feige.im.pojo.proto.IAck} message Ack message or plain object to encode
                         * @param {$protobuf.Writer} [writer] Writer to encode to
                         * @returns {$protobuf.Writer} Writer
                         */
                        Ack.encode = function encode(message, writer) {
                            if (!writer)
                                writer = $Writer.create();
                            if (message.id != null && Object.hasOwnProperty.call(message, "id"))
                                writer.uint32(/* id 1, wireType 2 =*/10).string(message.id);
                            if (message.status != null && Object.hasOwnProperty.call(message, "status"))
                                writer.uint32(/* id 2, wireType 2 =*/18).string(message.status);
                            return writer;
                        };

                        /**
                         * Encodes the specified Ack message, length delimited. Does not implicitly {@link com.feige.im.pojo.proto.Ack.verify|verify} messages.
                         * @function encodeDelimited
                         * @memberof com.feige.im.pojo.proto.Ack
                         * @static
                         * @param {com.feige.im.pojo.proto.IAck} message Ack message or plain object to encode
                         * @param {$protobuf.Writer} [writer] Writer to encode to
                         * @returns {$protobuf.Writer} Writer
                         */
                        Ack.encodeDelimited = function encodeDelimited(message, writer) {
                            return this.encode(message, writer).ldelim();
                        };

                        /**
                         * Decodes an Ack message from the specified reader or buffer.
                         * @function decode
                         * @memberof com.feige.im.pojo.proto.Ack
                         * @static
                         * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
                         * @param {number} [length] Message length if known beforehand
                         * @returns {com.feige.im.pojo.proto.Ack} Ack
                         * @throws {Error} If the payload is not a reader or valid buffer
                         * @throws {$protobuf.util.ProtocolError} If required fields are missing
                         */
                        Ack.decode = function decode(reader, length) {
                            if (!(reader instanceof $Reader))
                                reader = $Reader.create(reader);
                            var end = length === undefined ? reader.len : reader.pos + length, message = new $root.com.feige.im.pojo.proto.Ack();
                            while (reader.pos < end) {
                                var tag = reader.uint32();
                                switch (tag >>> 3) {
                                case 1:
                                    message.id = reader.string();
                                    break;
                                case 2:
                                    message.status = reader.string();
                                    break;
                                default:
                                    reader.skipType(tag & 7);
                                    break;
                                }
                            }
                            return message;
                        };

                        /**
                         * Decodes an Ack message from the specified reader or buffer, length delimited.
                         * @function decodeDelimited
                         * @memberof com.feige.im.pojo.proto.Ack
                         * @static
                         * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
                         * @returns {com.feige.im.pojo.proto.Ack} Ack
                         * @throws {Error} If the payload is not a reader or valid buffer
                         * @throws {$protobuf.util.ProtocolError} If required fields are missing
                         */
                        Ack.decodeDelimited = function decodeDelimited(reader) {
                            if (!(reader instanceof $Reader))
                                reader = new $Reader(reader);
                            return this.decode(reader, reader.uint32());
                        };

                        /**
                         * Verifies an Ack message.
                         * @function verify
                         * @memberof com.feige.im.pojo.proto.Ack
                         * @static
                         * @param {Object.<string,*>} message Plain object to verify
                         * @returns {string|null} `null` if valid, otherwise the reason why it is not
                         */
                        Ack.verify = function verify(message) {
                            if (typeof message !== "object" || message === null)
                                return "object expected";
                            if (message.id != null && message.hasOwnProperty("id"))
                                if (!$util.isString(message.id))
                                    return "id: string expected";
                            if (message.status != null && message.hasOwnProperty("status"))
                                if (!$util.isString(message.status))
                                    return "status: string expected";
                            return null;
                        };

                        /**
                         * Creates an Ack message from a plain object. Also converts values to their respective internal types.
                         * @function fromObject
                         * @memberof com.feige.im.pojo.proto.Ack
                         * @static
                         * @param {Object.<string,*>} object Plain object
                         * @returns {com.feige.im.pojo.proto.Ack} Ack
                         */
                        Ack.fromObject = function fromObject(object) {
                            if (object instanceof $root.com.feige.im.pojo.proto.Ack)
                                return object;
                            var message = new $root.com.feige.im.pojo.proto.Ack();
                            if (object.id != null)
                                message.id = String(object.id);
                            if (object.status != null)
                                message.status = String(object.status);
                            return message;
                        };

                        /**
                         * Creates a plain object from an Ack message. Also converts values to other types if specified.
                         * @function toObject
                         * @memberof com.feige.im.pojo.proto.Ack
                         * @static
                         * @param {com.feige.im.pojo.proto.Ack} message Ack
                         * @param {$protobuf.IConversionOptions} [options] Conversion options
                         * @returns {Object.<string,*>} Plain object
                         */
                        Ack.toObject = function toObject(message, options) {
                            if (!options)
                                options = {};
                            var object = {};
                            if (options.defaults) {
                                object.id = "";
                                object.status = "";
                            }
                            if (message.id != null && message.hasOwnProperty("id"))
                                object.id = message.id;
                            if (message.status != null && message.hasOwnProperty("status"))
                                object.status = message.status;
                            return object;
                        };

                        /**
                         * Converts this Ack to JSON.
                         * @function toJSON
                         * @memberof com.feige.im.pojo.proto.Ack
                         * @instance
                         * @returns {Object.<string,*>} JSON object
                         */
                        Ack.prototype.toJSON = function toJSON() {
                            return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
                        };

                        return Ack;
                    })();

                    return proto;
                })();

                return pojo;
            })();

            return im;
        })();

        return feige;
    })();

    return com;
})();

module.exports = $root;
