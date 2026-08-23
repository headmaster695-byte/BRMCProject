package com.jamesstevenson.brmc.client;

import com.jamesstevenson.brmc.gate.GateKind;
import com.jamesstevenson.brmc.gate.PresentationLock;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;

public class LinkedVolumeRenderState extends BlockEntityRenderState {
	public GateKind kind = GateKind.COMMONS;
	public PresentationLock.Crossing crossing = PresentationLock.Crossing.YELLOW_TO_YELLOW;
}
