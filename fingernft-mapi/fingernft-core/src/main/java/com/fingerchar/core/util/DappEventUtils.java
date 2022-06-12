package com.fingerchar.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.*;
import org.web3j.abi.datatypes.generated.Uint256;

import java.util.Arrays;
import java.util.HashMap;

public class DappEventUtils {

    private static final Logger logger = LoggerFactory.getLogger(DappEventUtils.class);

    public static final Event BUY_EVENT = new Event("Buy",
            Arrays.asList(new TypeReference<Address>(true) {
            }, new TypeReference<Uint256>(true) {
            }, new TypeReference<Uint256>() {
            }, new TypeReference<Address>() {
            }, new TypeReference<Address>() {
            }, new TypeReference<Uint256>() {
            }, new TypeReference<Uint256>() {
            }, new TypeReference<Address>() {
            }, new TypeReference<Uint256>() {
            }, new TypeReference<Uint256>() {
            }));
    public static final Event CANCEL_EVENT = new Event("Cancel",
            Arrays.asList(new TypeReference<Address>(true) {
            }, new TypeReference<Uint256>(true) {
            }, new TypeReference<Address>() {
            }, new TypeReference<Address>() {
            }, new TypeReference<Uint256>() {
            }, new TypeReference<Uint256>() {
            }));

    public static final Event TRANSFER_EVENT = new Event("Transfer",
            Arrays.asList(new TypeReference<Address>(true) {
            }, new TypeReference<Address>(true) {
            }, new TypeReference<Uint256>(true) {
            }));

    public static final Event TRANSFERBATCH_EVENT = new Event("TransferBatch",
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {
            }, new TypeReference<Address>(true) {
            }, new TypeReference<Address>(true) {
            }, new TypeReference<DynamicArray<Uint256>>() {
            }, new TypeReference<DynamicArray<Uint256>>() {
            }));

    public static final Event TRANSFERSINGLE_EVENT = new Event("TransferSingle",
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {
            }, new TypeReference<Address>(true) {
            }, new TypeReference<Address>(true) {
            }, new TypeReference<Uint256>() {
            }, new TypeReference<Uint256>() {
            }));


    public static final Event SECONDARYSALEFEES_EVENT = new Event("SecondarySaleFees",
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
            }, new TypeReference<DynamicArray<Address>>() {
            }, new TypeReference<DynamicArray<Uint256>>() {
            }));

    public static final Event CREATE_ERC721_EVENT = new Event("CreateERC721",
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {
            }, new TypeReference<Address>(false) {
            }, new TypeReference<Utf8String>(false) {
            }, new TypeReference<Utf8String>(false) {
            }));

    public static final Event CREATE_ERC1155_EVENT = new Event("CreateERC1155",
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {
            }, new TypeReference<Address>(false) {
            }, new TypeReference<Utf8String>(false) {
            }, new TypeReference<Utf8String>(false) {
            }));



    public static final String TRANSFER_TOPIC = EventEncoder.encode(TRANSFER_EVENT);

    public static final String CANCEL_TOPIC = EventEncoder.encode(CANCEL_EVENT);

    public static final String BUY_TOPIC = EventEncoder.encode(BUY_EVENT);


    public static final String TRANSFERBATCH_TOPIC = EventEncoder.encode(TRANSFERBATCH_EVENT);

    public static final String TRANSFERSINGLE_TOPIC = EventEncoder.encode(TRANSFERSINGLE_EVENT);

    public static final String SECONDARYSALEFEES_TOPIC = EventEncoder.encode(SECONDARYSALEFEES_EVENT);

    public static final String CREATE_ERC721_EVENT_TOPIC = EventEncoder.encode(CREATE_ERC721_EVENT);

    public static final String CREATE_ERC1155_EVENT_TOPIC = EventEncoder.encode(CREATE_ERC1155_EVENT);


    @SuppressWarnings("serial")
    public static final HashMap<String, Event> eventMap = new HashMap<String, Event>() {
        {
            put(TRANSFER_TOPIC, TRANSFER_EVENT);
            put(CANCEL_TOPIC, CANCEL_EVENT);
            put(BUY_TOPIC, BUY_EVENT);
            put(TRANSFERBATCH_TOPIC, TRANSFERBATCH_EVENT);
            put(TRANSFERSINGLE_TOPIC, TRANSFERSINGLE_EVENT);
            put(SECONDARYSALEFEES_TOPIC, SECONDARYSALEFEES_EVENT);
            put(CREATE_ERC721_EVENT_TOPIC, CREATE_ERC721_EVENT);
            put(CREATE_ERC1155_EVENT_TOPIC, CREATE_ERC1155_EVENT);
        }
    };

}

