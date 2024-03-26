package com.feige.fim.codec;

import com.feige.api.codec.PacketInterceptor;
import com.feige.api.session.Session;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

public class PacketHandler {

    private Node<PacketInterceptor> head;

    private Node<PacketInterceptor> tail;


    public PacketHandler(List<PacketInterceptor> packetInterceptors){
        if (CollectionUtils.isEmpty(packetInterceptors)){
            return;
        }
        Node<PacketInterceptor> curNode = head = build(packetInterceptors.get(0));
        for (int i = 0; i < packetInterceptors.size(); i++) {
            if (i == 0) continue;
            PacketInterceptor packetInterceptor = packetInterceptors.get(i);
            Node<PacketInterceptor> node = build(packetInterceptor);
            curNode.next = node;
            node.prev = curNode;
            curNode = node;
        }
        tail = curNode;
    }

    private Node<PacketInterceptor> build(PacketInterceptor interceptor){
        return new Node<>(interceptor);
    }

    void writePacket(Session session, Object packet){
        if (head != null){
            Node<PacketInterceptor> curNode = head;
            while (curNode != null){
                curNode.data.writePacket(session, packet);
                curNode = curNode.next;
            }
        }
    }


    void readPacket(Session session, Object packet){
        if (tail != null){
            Node<PacketInterceptor> curNode = tail;
            while (curNode != null){
                curNode.data.readPacket(session, packet);
                curNode = curNode.prev;
            }
        }
    }

    static class Node<T> {
        Node<T> prev;
        Node<T> next;
        final T data;

        public Node(T data){
            this.data = data;
        }

    }
}
