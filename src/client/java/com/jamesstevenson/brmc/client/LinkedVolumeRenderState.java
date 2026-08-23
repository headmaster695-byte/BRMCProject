package com.jamesstevenson.brmc.client;

import com.jamesstevenson.brmc.gate.DestinationVolume;
import com.jamesstevenson.brmc.gate.GateKind;
import com.jamesstevenson.brmc.gate.PortalLod;
import com.jamesstevenson.brmc.gate.PresentationLock;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.core.Direction;

public class LinkedVolumeRenderState extends BlockEntityRenderState {
	public GateKind kind = GateKind.COMMONS;
	public PresentationLock.Crossing crossing = PresentationLock.Crossing.YELLOW_TO_YELLOW;
	public PortalLod lod = PortalLod.MESH;
	public boolean draw;
	public boolean previewLies;
	public boolean farSideDistorts;
	public boolean destReady;
	public DestinationVolume destVolume = DestinationVolume.EMPTY;
	public Direction facing = Direction.EAST;
	public float anim;
	public float cameraDistance;
}
